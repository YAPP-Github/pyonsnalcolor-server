package com.pyonsnalcolor.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    
    // Access Token 파싱
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED, "Access Token이 만료되었습니다."),
    ACCESS_TOKEN_MALFORMED(UNAUTHORIZED, "Access Token의 형식이 유효하지 않습니다."),
    ACCESS_TOKEN_INVALID(UNAUTHORIZED,  "Access Token이 유효하지 않습니다."),
    ACCESS_TOKEN_NOT_BEARER(UNAUTHORIZED,  "Access Token이 Bearer 형식이 아닙니다."),

    // Refresh Token 검증
    REFRESH_TOKEN_NOT_EXIST(UNAUTHORIZED, "해당 Refresh Token을 가진 사용자가 없습니다."),
    REFRESH_TOKEN_MISMATCH(UNAUTHORIZED, "사용자의 Refresh Token과 일치하지 않습니다."),
    INVALID_OAUTH_ID(UNAUTHORIZED,  "해당 OAuth Id을 가진 사용자가 없습니다."),
    MEMBER_LOGOUT(UNAUTHORIZED,  "로그아웃된 사용자입니다."),

    // OAuth
    OAUTH_UNAUTHORIZED(UNAUTHORIZED, "OAuth 인증에 실패했습니다."),
    EMAIL_UNAUTHORIZED(UNAUTHORIZED, "이메일이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}