package com.pyonsnalcolor.auth.service;

import com.pyonsnalcolor.auth.dto.LoginResponseDto;
import com.pyonsnalcolor.exception.AuthException;
import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.member.enumtype.Nickname;
import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.enumtype.Role;
import com.pyonsnalcolor.auth.RedisUtil;
import com.pyonsnalcolor.auth.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.auth.dto.NicknameRequestDto;
import com.pyonsnalcolor.auth.dto.TokenDto;
import com.pyonsnalcolor.auth.AuthUserDetails;
import com.pyonsnalcolor.auth.jwt.JwtTokenProvider;
import com.pyonsnalcolor.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class MemberService {

    @Value("${jwt.access-token.validity}")
    private long accessTokenValidity;

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    private static final String LOGOUT_BLACKLIST = "logout";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public LoginResponseDto oAuthLogin(OAuthType oAuthType, String email) {
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

    private LoginResponseDto join(OAuthType OAuthType, String email) {
        String oauthId = OAuthType.addOAuthTypeHeaderWithEmail(email);
        TokenDto tokenDto = jwtTokenProvider.createAccessAndRefreshTokenDto(oauthId);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        Member member = Member.builder()
                .email(email)
                .nickname(Nickname.getRandomNickname())
                .refreshToken(refreshToken)
                .oAuthId(oauthId)
                .OAuthType(OAuthType)
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(member);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isFirstLogin(true)
                .build();
    }

    private String updateAccessToken(Member member) {
        String oauthId = member.getOAuthId();
        String refreshToken = member.getRefreshToken();
        String newAccessToken = jwtTokenProvider.createBearerTokenWithValidity(oauthId, accessTokenValidity);

        validateRefreshToken(oauthId, refreshToken);

        return newAccessToken;
    }

    public TokenDto reissueAccessToken(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(REFRESH_TOKEN_NOT_EXIST));

        String newAccessToken = updateAccessToken(member);

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void validateRefreshToken(String oauthId, String refreshToken) {
        String findRefreshToken = memberRepository.findRefreshTokenByoAuthId(oauthId)
                .orElseThrow(() -> new AuthException(INVALID_OAUTH_ID));

        if (!findRefreshToken.equals(refreshToken)) {
            throw new AuthException(REFRESH_TOKEN_MISMATCH);
        }
    }

    public void withdraw(AuthUserDetails authUserDetails) {
        Member member = authUserDetails.getMember();
        memberRepository.delete(member);

        SecurityContextHolder.clearContext();
    }

    public MemberInfoResponseDto getMemberInfo(AuthUserDetails authUserDetails) {
        Member member = authUserDetails.getMember();
        return new MemberInfoResponseDto(member);
    }

    public void updateNickname(
            AuthUserDetails authUserDetails,
            NicknameRequestDto nicknameRequestDto
    ) {
        Member member = authUserDetails.getMember();
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
}