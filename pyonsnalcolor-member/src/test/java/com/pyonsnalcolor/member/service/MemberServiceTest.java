package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.domain.member.Member;
import com.pyonsnalcolor.domain.member.MemberRepository;
import com.pyonsnalcolor.domain.member.enumtype.OAuthType;
import com.pyonsnalcolor.domain.member.enumtype.Role;
import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.dto.TokenDto;
import com.pyonsnalcolor.member.entity.CustomUserDetails;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private String email;
    private OAuthType oAuthType;
    private String oAuthId;
    private Member member;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        oAuthType = OAuthType.APPLE;
        oAuthId = oAuthType.addOAuthTypeHeaderWithEmail(email);
        member = Member.builder()
                .email(email)
                .OAuthType(oAuthType)
                .oauthId(oAuthId)
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("첫 OAuth 로그인일 경우, 회원가입")
    void join() {
        // given
        memberService.login(oAuthType, email);

        // when
        Member member = memberRepository.findByOauthId(oAuthId).get();

        // then
        Assertions.assertAll(
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(member.getOauthId(), oAuthId),
                () -> assertEquals(member.getOAuthType(), oAuthType)
        );
    }

    @Test
    @Order(2)
    @DisplayName("OAuth 재로그인일 경우, access token만 갱신")
    void updateAccessToken() {
        // given
        memberRepository.save(member);
        TokenDto tokenDto = memberService.login(oAuthType, email);

        // when
        Member member = memberRepository.findByOauthId(oAuthId).get();

        // then
        Assertions.assertAll(
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(member.getOauthId(), oAuthId),
                () -> assertEquals(member.getOAuthType(), oAuthType)
        );
    }

    @Test
    @Order(3)
    @DisplayName("사용자 정보 조회하기")
    void getMemberInfo() {
        // given
        memberRepository.save(member);
        setAuthentication();

        // when
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(customUserDetails);

        // then
        Assertions.assertAll(
                () -> assertEquals(memberInfoResponseDto.getEmail(), email),
                () -> assertEquals(memberInfoResponseDto.getOauthId(), oAuthId),
                () -> assertEquals(memberInfoResponseDto.getOAuthType(), oAuthType.toString())
        );
    }

    @Test
    @Order(4)
    @DisplayName("닉네임 변경하기")
    void updateNickname() throws Exception {
        // given
        memberRepository.save(member);
        setAuthentication();
        String updatedNickname = "새로운 닉네임";
        memberService.updateNickname(customUserDetails, new NicknameRequestDto(updatedNickname));

        // when
        Member findMember = memberRepository.findByOauthId(oAuthId)
                .orElseThrow(Exception::new);

        // then
        assertEquals(findMember.getNickname(), updatedNickname);
    }

    // CustomUserDetails로 Member 객체를 매핑하기 때문에 필요
    private void setAuthentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        customUserDetails = new CustomUserDetails(member);
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                customUserDetails,
                "",
                customUserDetails.getAuthorities()));
    }
}