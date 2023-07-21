package com.pyonsnalcolor.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum PushErrorCode implements ErrorCode {

    KEYWORD_ALREADY_EXIST(BAD_REQUEST, "이미 존재하는 키워드입니다."),
    KEYWORD_NOT_EXIST(BAD_REQUEST, "키워드가 존재하지 않습니다."),
    KEYWORD_LIMIT_EXCEEDED(BAD_REQUEST, "등록 가능한 키워드 수를 초과하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}