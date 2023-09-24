package com.pyonsnalcolor.member.service;

import com.pyonsnalcolor.member.entity.Member;
import com.pyonsnalcolor.member.repository.MemberRepository;
import com.pyonsnalcolor.member.dto.LoginRequestDto;
import com.pyonsnalcolor.member.dto.LoginResponseDto;
import com.pyonsnalcolor.member.dto.MemberInfoResponseDto;
import com.pyonsnalcolor.member.dto.NicknameRequestDto;
import com.pyonsnalcolor.member.enumtype.OAuthType;
import com.pyonsnalcolor.member.enumtype.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@SpringBootTest(classes = AuthService.class)
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    private String email;
    private OAuthType oAuthType;
    private String oAuthId;
    private Member member;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        oAuthType = OAuthType.APPLE;
        oAuthId = oAuthType.addOAuthTypeHeaderWithEmail(email);
        member = Member.builder()
                .email(email)
                .OAuthType(oAuthType)
                .oAuthId(oAuthId)
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("첫 OAuth 로그인일 경우, 회원가입")
    void oAuthLogin_join() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .token("token")
                .oauthType("apple").build();
        authService.oAuthLogin(loginRequestDto);

        // when
        Member member = memberRepository.findByoAuthId(oAuthId)
                .orElseThrow(IllegalArgumentException::new);

        // then
        Assertions.assertAll(
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(member.getOAuthId(), oAuthId),
                () -> assertEquals(member.getOAuthType(), oAuthType)
        );
    }

    @Test
    @Order(2)
    @DisplayName("OAuth 재로그인일 경우, access token만 갱신")
    void oAuthLogin_reLogin() {
        // given
        memberRepository.save(member); // 회원 가입 미리 되어있는 경우
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .token("token")
                .oauthType("apple").build();
        LoginResponseDto loginResponseDto = authService.oAuthLogin(loginRequestDto);

        // when
        String refreshToken = loginResponseDto.getRefreshToken();
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(IllegalArgumentException::new);

        // then
        Assertions.assertAll(
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(member.getOAuthId(), oAuthId),
                () -> assertEquals(member.getOAuthType(), oAuthType)
        );
    }

    @Test
    @Order(3)
    @DisplayName("사용자 정보 조회하기")
    void getMemberInfo() {
        // given
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();

        // when
        MemberInfoResponseDto memberInfoResponseDto = authService.getMemberInfo(memberId);

        // then
        Assertions.assertAll(
                () -> assertEquals(memberInfoResponseDto.getEmail(), email),
                () -> assertEquals(memberInfoResponseDto.getOauthId(), oAuthId),
                () -> assertEquals(memberInfoResponseDto.getOauthType(), oAuthType.toString())
        );
    }

    @Test
    @Order(4)
    @DisplayName("닉네임 변경하기")
    void updateNickname() throws Exception {
        // given
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();
        String updatedNickname = "새로운 닉네임";
        authService.updateNickname(memberId, new NicknameRequestDto(updatedNickname));

        // when
        Member findMember = memberRepository.getReferenceById(memberId);

        // then
        assertEquals(findMember.getNickname(), updatedNickname);
    }
}
