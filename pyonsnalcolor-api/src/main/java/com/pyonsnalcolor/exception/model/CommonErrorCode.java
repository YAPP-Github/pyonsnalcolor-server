package com.pyonsnalcolor.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    SERVER_UNAVAILABLE(SERVICE_UNAVAILABLE, "서버에 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}