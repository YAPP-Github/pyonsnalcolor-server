package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.member.dto.*;
import com.pyonsnalcolor.member.enumtype.Nickname;
import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.enumtype.Role;
import com.pyonsnalcolor.member.oauth.OAuthClient;
import com.pyonsnalcolor.member.oauth.OAuthLoginService;
import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.member.RedisUtil;
import com.pyonsnalcolor.member.security.JwtTokenProvider;
import com.pyonsnalcolor.product.enumtype.ProductStoreType;
import com.pyonsnalcolor.push.PushProductStore;
import com.pyonsnalcolor.push.repository.PushKeywordRepository;
import com.pyonsnalcolor.push.repository.PushProductStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class AuthService {

    @Value("${jwt.access-token.validity}")
    private long accessTokenValidity;

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    private static final String LOGOUT_BLACKLIST = "logout";

    private final MemberRepository memberRepository;
    private final PushProductStoreRepository pushProductStoreRepository;
    private final PushKeywordRepository pushKeywordRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final OAuthLoginService oAuthLoginService;

    public LoginResponseDto oAuthLogin(LoginRequestDto loginRequestDto) {
        OAuthClient oAuthClient = oAuthLoginService.getOAuthLoginClient(loginRequestDto);
        String email = oAuthClient.getEmail(loginRequestDto);
        OAuthType oAuthType = oAuthClient.oAuthType();
        String oauthId = oAuthType.addOAuthTypeHeaderWithEmail(email);

        return memberRepository.findByoAuthId(oauthId)
                .map(this::reLogin)
                .orElseGet(() ->  join(oAuthType, email));
    }

    private LoginResponseDto reLogin(Member member) {
        String newAccessToken = updateAccessToken(member);
        String refreshToken = member.getRefreshToken();

        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .isFirstLogin(false)
                .build();
    }

    public LoginResponseDto join(OAuthType oAuthType, String email) {
        String oauthId = oAuthType.addOAuthTypeHeaderWithEmail(email);
        TokenDto tokenDto = jwtTokenProvider.createAccessAndRefreshTokenDto(oauthId);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        Member member = Member.builder()
                .email(email)
                .nickname(Nickname.getRandomNickname())
                .refreshToken(refreshToken)
                .oAuthId(oauthId)
                .OAuthType(oAuthType)
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(member);
        createPushProductStores(member);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isFirstLogin(true)
                .build();
    }

    public void createPushProductStores (Member member) {
        List<PushProductStore> pushProductStores = Arrays.stream(ProductStoreType.values())
                .map( i -> PushProductStore.builder()
                        .productStoreType(i)
                        .member(member)
                        .isSubscribed(true)
                        .updatedTime(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        pushProductStoreRepository.saveAll(pushProductStores);
    }

    private String updateAccessToken(Member member) {
        String oauthId = member.getOAuthId();
        String refreshToken = member.getRefreshToken();
        String newAccessToken = jwtTokenProvider.createBearerTokenWithValidity(oauthId, accessTokenValidity);

        validateRefreshToken(oauthId, refreshToken);
        return newAccessToken;
    }

    public LoginResponseDto reissueAccessToken(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new PyonsnalcolorAuthException(REFRESH_TOKEN_NOT_EXIST));

        String newAccessToken = updateAccessToken(member);

        return LoginResponseDto.builder()
                .isFirstLogin(false)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void validateRefreshToken(String oauthId, String refreshToken) {
        String findRefreshToken = memberRepository.findRefreshTokenByoAuthId(oauthId)
                .orElseThrow(() -> new PyonsnalcolorAuthException(INVALID_OAUTH_ID));

        if (!findRefreshToken.equals(refreshToken)) {
            throw new PyonsnalcolorAuthException(REFRESH_TOKEN_MISMATCH);
        }
    }

    public void withdraw(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        deletePushKeyword(member);
        deletePushProductStore(member);
        memberRepository.delete(member);

        SecurityContextHolder.clearContext();
    }

    private void deletePushKeyword(Member member) {
        pushKeywordRepository.findByMember(member)
                .forEach(pushKeywordRepository::delete);
    }

    private void deletePushProductStore(Member member) {
        pushProductStoreRepository.findByMember(member)
                .forEach(pushProductStoreRepository::delete);
    }

    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        return new MemberInfoResponseDto(member);
    }

    public void updateNickname(
            Long memberId,
            NicknameRequestDto nicknameRequestDto
    ) {
        Member member = memberRepository.getReferenceById(memberId);
        String updatedNickname = nicknameRequestDto.getNickname();

        member.updateNickname(updatedNickname);
        memberRepository.save(member);
    }

    public void logout(TokenDto tokenDto) {
        String bearerToken = tokenDto.getAccessToken();
        String accessToken = jwtTokenProvider.resolveBearerToken(bearerToken);
        Long expirationTime = jwtTokenProvider.getExpirationTime(accessToken);

        redisUtil.setBlackList(accessToken, LOGOUT_BLACKLIST, expirationTime);
    }

    public JoinStatusResponseDto getJoinStatus(LoginRequestDto loginRequestDto) {
        OAuthClient oAuthClient = oAuthLoginService.getOAuthLoginClient(loginRequestDto);
        String email = oAuthClient.getEmail(loginRequestDto);
        OAuthType oAuthType = oAuthClient.oAuthType();
        String oauthId = oAuthType.addOAuthTypeHeaderWithEmail(email);

        Boolean isJoined = memberRepository.findByoAuthId(oauthId).isPresent();
        return JoinStatusResponseDto.builder()
                .isJoined(isJoined)
                .build();
    }
}