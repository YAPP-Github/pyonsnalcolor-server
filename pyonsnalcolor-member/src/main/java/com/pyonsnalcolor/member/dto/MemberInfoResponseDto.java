package com.pyonsnalcolor.member.dto;

import com.pyonsnalcolor.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponseDto {

    private Long memberId;

    private String oauthId;

    private String nickname;

    private String email;

    public MemberInfoResponseDto(Member member) {
        this.memberId  = member.getId();
        this.oauthId  = member.getOauthId();
        this.nickname  = member.getNickname();
        this.email  = member.getEmail();
    }
}