package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.auth.Member;
import com.pyonsnalcolor.auth.MemberRepository;
import com.pyonsnalcolor.auth.enumtype.OAuthType;
import com.pyonsnalcolor.auth.enumtype.Role;
import com.pyonsnalcolor.auth.service.MemberService;
import com.pyonsnalcolor.push.dto.PushProductStoreRequestDto;
import com.pyonsnalcolor.push.dto.PushProductStoreResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class PushProductStoreServiceTest {

    @Autowired
    private PushProductStoreService pushProductStoreService;


    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입하면 모든 편의점을 구독 신청합니다.")
    @Test
    void getPushProductStores() {
        Member member =  Member.builder()
                .email("sample@gmail.com")
                .OAuthType(OAuthType.APPLE)
                .oAuthId("apple-sample@gmail.com")
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER).build();
        Member savedMember = memberRepository.save(member);
        memberService.createPushProductStores(member);

        List<PushProductStoreResponseDto> result = pushProductStoreService.getPushProductStores(savedMember.getId());

        assertAll(
                () -> assertThat(result.size()).isEqualTo(8),
                () -> assertThat(result).extracting("productStore")
                        .contains("PB_CU", "PB_GS25", "PB_EMART24", "PB_SEVEN_ELEVEN",
                                "EVENT_CU", "EVENT_GS25", "EVENT_EMART24", "EVENT_SEVEN_ELEVEN"),
                () -> assertThat(result).extracting("isSubscribed")
                        .containsOnly(true));
    }

    @DisplayName("편의점 푸시 알림을 구독 취소합니다.")
    @Test
    void subscribePushProductStores() {
        Member member =  Member.builder()
                .email("sample@gmail.com")
                .OAuthType(OAuthType.APPLE)
                .oAuthId("apple-sample@gmail.com")
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER).build();
        Member savedMember = memberRepository.save(member);
        memberService.createPushProductStores(savedMember);

        PushProductStoreRequestDto requestDto = PushProductStoreRequestDto.builder()
                .productStores(List.of("EVENT_CU", "PB_CU"))
                .build();

        pushProductStoreService.unsubscribePushProductStores(savedMember.getId(), requestDto);
        List<PushProductStoreResponseDto> result = pushProductStoreService.getPushProductStores(savedMember.getId());

        assertAll(
                () -> assertThat(result).filteredOn(PushProductStoreResponseDto::getProductStore, "PB_CU")
                        .filteredOn(PushProductStoreResponseDto::getIsSubscribed, false),
                () -> assertThat(result).filteredOn(PushProductStoreResponseDto::getProductStore,"EVENT_CU")
                        .filteredOn(PushProductStoreResponseDto::getIsSubscribed, false));
    }
}
