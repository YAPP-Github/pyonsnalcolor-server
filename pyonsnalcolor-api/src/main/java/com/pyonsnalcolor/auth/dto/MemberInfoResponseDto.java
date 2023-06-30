package com.pyonsnalcolor.auth.dto;

import com.pyonsnalcolor.member.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponseDto {

    private Long memberId;

    private String oAuthId;

    private String oAuthType;

    private String nickname;

    private String email;

    public MemberInfoResponseDto(Member member) {
        this.memberId  = member.getId();
        this.oAuthId = member.getOAuthId();
        this.oAuthType = member.getOAuthType().toString();
        this.nickname  = member.getNickname();
        this.email  = member.getEmail();
    }
}