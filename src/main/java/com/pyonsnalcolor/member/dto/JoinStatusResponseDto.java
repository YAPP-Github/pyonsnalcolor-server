package com.pyonsnalcolor.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "회원가입 여부 Response DTO")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JoinStatusResponseDto {

    @Schema(description = "회원가입 여부")
    @NotBlank
    private Boolean isJoined;
}