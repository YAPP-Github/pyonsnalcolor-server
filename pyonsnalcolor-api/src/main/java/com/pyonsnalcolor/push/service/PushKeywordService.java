package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.auth.AuthUserDetails;
import com.pyonsnalcolor.exception.PyonsnalcolorPushException;
import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.push.PushKeyword;
import com.pyonsnalcolor.push.dto.PushKeywordRequestDto;
import com.pyonsnalcolor.push.dto.PushKeywordResponseDto;
import com.pyonsnalcolor.push.repository.PushKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pyonsnalcolor.exception.model.PushErrorCode.*;

@Service
@RequiredArgsConstructor
public class PushKeywordService {

    private final PushKeywordRepository pushKeywordRepository;
    private static final int PUSH_KEYWORD_NUMBER = 3;

    public PushKeyword createPushKeyword(AuthUserDetails authUserDetails, PushKeywordRequestDto pushKeywordRequestDto) {
        Member member = authUserDetails.getMember();
        String pushKeywordName = pushKeywordRequestDto.getName();

        validatePushKeywordExist(member, pushKeywordName);
        validatePushKeywordNumber(member);

        PushKeyword newPushKeyword = PushKeyword.builder()
                .member(member)
                .name(pushKeywordName)
                .build();
        return pushKeywordRepository.save(newPushKeyword);
    }

    private void validatePushKeywordExist(Member member, String pushKeywordName) {
        pushKeywordRepository.findByMemberAndName(member, pushKeywordName)
                .ifPresent( p -> {
                    throw new PyonsnalcolorPushException(KEYWORD_ALREADY_EXIST);
                });
    }

    private void validatePushKeywordNumber(Member member) {
        List<PushKeyword> result = pushKeywordRepository.findByMember(member);
        if (result.size() >= PUSH_KEYWORD_NUMBER) {
            throw new PyonsnalcolorPushException(KEYWORD_LIMIT_EXCEEDED);
        }
    }

    public void deletePushKeyword(AuthUserDetails authUserDetails, Long keywordId) {
        Member member = authUserDetails.getMember();
        PushKeyword pushKeyword = pushKeywordRepository.findByMemberAndId(member, keywordId)
                .orElseThrow(() -> new PyonsnalcolorPushException(KEYWORD_NOT_EXIST));
        pushKeywordRepository.delete(pushKeyword);
    }

    public List<PushKeywordResponseDto> getPushKeywordResponseDto(AuthUserDetails authUserDetails) {
        Member member = authUserDetails.getMember();
        List<PushKeyword> pushKeywords = pushKeywordRepository.findByMember(member);

        return  pushKeywords.stream()
                .map(pushKeyword -> PushKeywordResponseDto.builder()
                        .id(pushKeyword.getId())
                        .name(pushKeyword.getName())
                        .build())
                .collect(Collectors.toList());
    }
}