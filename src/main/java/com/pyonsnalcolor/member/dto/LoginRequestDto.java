package com.pyonsnalcolor.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "OAuth 로그인용 Request DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @Schema(description = "OAuth 로그인 후 받은 토큰")
    @NotBlank(message = "OAuth 타입별로 로그인 후 받은 토큰을 입력해야 합니다.")
    private String token;

    @Schema(description = "OAuth 타입")
    @NotBlank(message = "OAuth 타입을 입력해야 합니다.")
    private String oauthType;
}