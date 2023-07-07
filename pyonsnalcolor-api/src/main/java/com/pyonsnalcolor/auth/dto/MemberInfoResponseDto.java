package com.pyonsnalcolor.auth.dto;

import com.pyonsnalcolor.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Schema(description = "사용자 정보 Response DTO")
@Getter
public class MemberInfoResponseDto {

    @Schema(description = "OAuth ID", example = "apple-user@gmail.com")
    @NotBlank
    private String oauthId;

    @Schema(description = "OAuth 타입", example = "APPLE")
    @NotBlank
    private String oauthType;

    @NotBlank
    private Long memberId;

    @NotBlank
    private String nickname;

    @NotBlank
    private String email;

    public MemberInfoResponseDto(Member member) {
        this.memberId  = member.getId();
        this.oauthId = member.getOAuthId();
        this.oauthType = member.getOAuthType().toString();
        this.nickname  = member.getNickname();
        this.email  = member.getEmail();
    }
}