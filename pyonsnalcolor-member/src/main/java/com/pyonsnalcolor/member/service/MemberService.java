package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.domain.member.Member;
import com.pyonsnalcolor.domain.member.enumtype.Nickname;
import com.pyonsnalcolor.domain.member.enumtype.OAuthType;
import com.pyonsnalcolor.domain.member.enumtype.Role;
import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.jwt.JwtTokenProvider;
import com.pyonsnalcolor.domain.member.MemberRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class MemberService {

    @Value("${jwt.access-token.validity}")
    private long accessTokenValidity;

    @Value("${jwt.bearer.header}")
    private String bearerHeader;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDto login(OAuthType OAuthType, String email) {
        String oauthId = OAuthType.addOAuthTypeHeaderWithEmail(email);

        return memberRepository.findByOauthId(oauthId)
                .map(this::updateAccessToken)
                .orElseGet(() ->  join(OAuthType, email));
    }

    private TokenDto updateAccessToken(Member member) {
        String oauthId = member.getOauthId();
        String refreshToken = member.getRefreshToken();
        String newAccessToken = jwtTokenProvider.createTokenWithValidity(oauthId, accessTokenValidity);

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private TokenDto join(OAuthType OAuthType, String email) {
        String oauthId = OAuthType.addOAuthTypeHeaderWithEmail(email);
        TokenDto tokenDto = jwtTokenProvider.createAccessAndRefreshTokenDto(oauthId);
        String refreshToken = tokenDto.getRefreshToken();

        Member member = Member.builder()
                .email(email)
                .nickname(Nickname.getRandomNickname())
                .refreshToken(refreshToken)
                .oauthId(oauthId)
                .OAuthType(OAuthType)
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(member);

        return tokenDto;
    }

    public TokenDto reissueAccessToken(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        String oauthId = member.getOauthId();
        String refreshToken = member.getRefreshToken();

        validateRefreshToken(oauthId, refreshToken);
        return updateAccessToken(member);
    }

    private void validateRefreshToken(String oauthId, String refreshToken) {
        Object findRefreshToken = memberRepository.findRefreshTokenByOauthId(oauthId)
                .orElseThrow(() -> new JwtException("사용자의 refresh token이 존재하지 않습니다."));

        if (!findRefreshToken.equals(refreshToken)) {
            throw new JwtException("사용자의 refresh token과 일치하지 않습니다.");
        }
    }

    public void withdraw(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        memberRepository.delete(member);

        SecurityContextHolder.clearContext();
    }

    public MemberInfoResponseDto getMemberInfo(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        return new MemberInfoResponseDto(member);
    }

    public void updateNickname(Authentication authentication, NicknameRequestDto nicknameRequestDto) {
        Member member = findMemberByAuthentication(authentication);
        String updatedNickname = nicknameRequestDto.getNickname();

        member.updateNickname(updatedNickname);
        memberRepository.save(member);
    }

    private Member findMemberByAuthentication(Authentication authentication) {
        String oauthId = authentication.getName();

        return memberRepository.findByOauthId(oauthId)
                .orElseThrow(
                () -> new IllegalArgumentException("해당 토큰의 사용자가 존재하지 않습니다.")
        );
    }
}