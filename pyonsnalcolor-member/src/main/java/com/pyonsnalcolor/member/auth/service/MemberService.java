package com.pyonsnalcolor.member.auth.service;

import com.pyonsnalcolor.domain.member.Member;
import com.pyonsnalcolor.domain.member.enumtype.Nickname;
import com.pyonsnalcolor.domain.member.enumtype.OAuthType;
import com.pyonsnalcolor.domain.member.enumtype.Role;
import com.pyonsnalcolor.member.auth.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.auth.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.auth.dto.TokenDto;
import com.pyonsnalcolor.member.auth.CustomUserDetails;
import com.pyonsnalcolor.member.auth.jwt.JwtTokenProvider;
import com.pyonsnalcolor.domain.member.MemberRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

    public TokenDto login(OAuthType oAuthType, String email) {
        String oauthId = oAuthType.addOAuthTypeHeaderWithEmail(email);

        return memberRepository.findByoAuthId(oauthId)
                .map(this::updateAccessToken)
                .orElseGet(() ->  join(oAuthType, email));
    }

    private TokenDto updateAccessToken(Member member) {
        String oauthId = member.getOAuthId();
        String refreshToken = member.getRefreshToken();
        String newAccessToken = jwtTokenProvider.createTokenWithValidity(oauthId, accessTokenValidity);

        validateRefreshToken(oauthId, refreshToken);

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
                .oAuthId(oauthId)
                .OAuthType(OAuthType)
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(member);

        return tokenDto;
    }

    public TokenDto reissueAccessToken(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new JwtException("해당 refresh token을 가진 사용자가 존재하지 않습니다."));
        return updateAccessToken(member);
    }

    private void validateRefreshToken(String oauthId, String refreshToken) {
        String findRefreshToken = memberRepository.findRefreshTokenByoAuthId(oauthId)
                .orElseThrow(() -> new JwtException("사용자의 refresh token이 존재하지 않습니다."));

        if (!findRefreshToken.equals(refreshToken)) {
            throw new JwtException("사용자의 refresh token과 일치하지 않습니다.");
        }
    }

    public void withdraw(CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        memberRepository.delete(member);

        SecurityContextHolder.clearContext();
    }

    public MemberInfoResponseDto getMemberInfo(CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        return new MemberInfoResponseDto(member);
    }

    public void updateNickname(
            CustomUserDetails customUserDetails,
            NicknameRequestDto nicknameRequestDto
    ) {
        Member member = customUserDetails.getMember();
        String updatedNickname = nicknameRequestDto.getNickname();

        member.updateNickname(updatedNickname);
        memberRepository.save(member);
    }
}