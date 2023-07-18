package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.auth.Member;
import com.pyonsnalcolor.auth.MemberRepository;
import com.pyonsnalcolor.auth.enumtype.OAuthType;
import com.pyonsnalcolor.auth.enumtype.Role;
import com.pyonsnalcolor.exception.PyonsnalcolorPushException;
import com.pyonsnalcolor.push.PushKeyword;
import com.pyonsnalcolor.push.dto.PushKeywordRequestDto;
import com.pyonsnalcolor.push.repository.PushKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@Transactional
@SpringBootTest
class PushKeywordServiceTest {

    @Autowired
    private PushKeywordService pushKeywordService;

    @Autowired
    private PushKeywordRepository pushKeywordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("푸시 키워드를 조회합니다.")
    @Test
    void savePushKeyword() {
        Member member =  Member.builder()
                .email("sample@gmail.com")
                .OAuthType(OAuthType.APPLE)
                .oAuthId("apple-sample@gmail.com")
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER).build();
        Member savedMember = memberRepository.save(member);

        pushKeywordService.createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("포켓몬"));
        pushKeywordService.createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("짱구"));
        pushKeywordService.createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("제로"));

        List<PushKeyword> result = pushKeywordRepository.findByMember(member);
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.contains("포켓몬")),
                () -> assertThat(result.contains("짱구")),
                () -> assertThat(result.contains("제로")));
    }

    @DisplayName("잘못된 형식의 푸시 키워드를 저장할 경우 예외를 반환합니다.")
    @Test
    void invalidateFormatOfPushKeyword() {
        Member member =  Member.builder()
                .email("sample@gmail.com")
                .OAuthType(OAuthType.APPLE)
                .oAuthId("apple-sample@gmail.com")
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER).build();
        Member savedMember = memberRepository.save(member);

        PushKeywordRequestDto pushKeywordRequestDto = PushKeywordRequestDto.builder().name("!@초코").build();

        assertThatThrownBy(() -> pushKeywordService.createPushKeyword(savedMember.getId(), pushKeywordRequestDto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이미 저장한 푸시 키워드일 경우 예외를 반환합니다.")
    @Test
    void duplicatePushKeyword() {
        Member member =  Member.builder()
                .email("sample@gmail.com")
                .OAuthType(OAuthType.APPLE)
                .oAuthId("apple-sample@gmail.com")
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER).build();
        Member savedMember = memberRepository.save(member);

        PushKeywordRequestDto pushKeywordRequestDto = PushKeywordRequestDto.builder().name("초코").build();
        pushKeywordService.createPushKeyword(savedMember.getId(), pushKeywordRequestDto);

        assertThatThrownBy(() -> pushKeywordService.createPushKeyword(savedMember.getId(), pushKeywordRequestDto))
                .isInstanceOf(PyonsnalcolorPushException.class);
    }


    @DisplayName("3개 이상의 푸시 키워드를 저장할 경우 예외를 반환합니다.")
    @Test
    void exceedLimitOfPushKeywordNumber() {
        Member member =  Member.builder().email("sample@gmail.com")
                .OAuthType(OAuthType.APPLE)
                .oAuthId("apple-sample@gmail.com")
                .refreshToken("refreshToken")
                .role(Role.ROLE_USER).build();
        Member savedMember = memberRepository.save(member);

        pushKeywordService.createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("포켓몬"));
        pushKeywordService.createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("짱구"));
        pushKeywordService.createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("제로"));

        assertThatThrownBy(() -> pushKeywordService
                .createPushKeyword(savedMember.getId(), new PushKeywordRequestDto("슈가")))
                .isInstanceOf(PyonsnalcolorPushException.class);
    }
}
