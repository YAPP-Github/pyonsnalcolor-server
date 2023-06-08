package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.entity.enumtype.LoginType;
import com.pyonsnalcolor.member.entity.enumtype.Role;
import com.pyonsnalcolor.member.jwt.JwtTokenProvider;
import com.pyonsnalcolor.member.repository.MemberRepository;
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
    public TokenDto join(LoginType loginType, String email) {
        String oauthId = loginType.addLoginTypeHeaderWithEmail(email);
        TokenDto tokenDto = jwtTokenProvider.createAccessAndRefreshTokenDto(oauthId);
        String refreshToken = tokenDto.getRefreshToken();

        boolean isMemberEmpty = memberRepository.findByOauthId(oauthId).isEmpty();
        if (isMemberEmpty) {
            Member member = Member.builder()
                    .email(email)
                    .refreshToken(refreshToken)
                    .oauthId(oauthId)
                    .loginType(loginType)
                    .role(Role.ROLE_USER)
                    .build();
            memberRepository.save(member);
        }
        return tokenDto;
    }
}