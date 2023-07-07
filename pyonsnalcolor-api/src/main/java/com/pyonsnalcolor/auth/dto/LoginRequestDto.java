package com.pyonsnalcolor.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "OAuth 로그인용 Request DTO")
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "OAuth 로그인 후 받은 토큰")
    @NotBlank
    private String token;
}