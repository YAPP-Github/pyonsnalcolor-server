package com.pyonsnalcolor.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "OAuth 로그인 후 Response DTO", required = true)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    @Schema(description = "최초 로그인(회원가입)인지, 다시 로그인(로그아웃 후)인지 구분용", required = true)
    @NotBlank
    private Boolean isFirstLogin;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}