package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.domain.member.Member;
import com.pyonsnalcolor.domain.member.enumtype.OAuthType;
import com.pyonsnalcolor.domain.member.enumtype.Role;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.jwt.JwtTokenProvider;
import com.pyonsnalcolor.domain.member.MemberRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public TokenDto join(OAuthType OAuthType, String email) {
        String oauthId = OAuthType.addOAuthTypeHeaderWithEmail(email);
        TokenDto tokenDto = jwtTokenProvider.createAccessAndRefreshTokenDto(oauthId);
        String refreshToken = tokenDto.getRefreshToken();

        boolean isMemberEmpty = memberRepository.findByOauthId(oauthId).isEmpty();
        if (isMemberEmpty) {
            Member member = Member.builder()
                    .email(email)
                    .refreshToken(refreshToken)
                    .oauthId(oauthId)
                    .OAuthType(OAuthType)
                    .role(Role.ROLE_USER)
                    .build();
            memberRepository.save(member);
        }
        return tokenDto;
    }

    public TokenDto reissueAccessToken(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        String resolvedToken = resolveBearerToken(refreshToken);
        String oauthId = jwtTokenProvider.getOauthId(resolvedToken);

        validateRefreshToken(oauthId, refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(oauthId);
        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void validateRefreshToken(String oauthId, String refreshToken) {
        Object findRefreshToken = memberRepository.findRefreshTokenByOauthId(oauthId)
                .orElseThrow(() -> new JwtException("사용자의 refreshToken이 존재하지 않습니다."));

        if (!findRefreshToken.equals(refreshToken)) {
            throw new JwtException("사용자의 refreshToken와 일치하지 않습니다.");
        }
    }

    private String resolveBearerToken(String token) {
        if (token != null && token.startsWith(bearerHeader)) {
            return token.substring(bearerHeader.length());
        }
        throw new JwtException("사용자 token이 Bearer 형식에 맞지 않습니다.");
    }
}