package com.pyonsnalcolor.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private Boolean isFirstLogin; // 최초 로그인(회원가입)인지, 다시 로그인(로그아웃 후)인지 구분

    private String accessToken;

    private String refreshToken;
}