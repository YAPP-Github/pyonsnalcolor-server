package com.pyonsnalcolor.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum PushKeywordErrorCode implements ErrorCode {

    INVALID_KEYWORD_FORMAT(BAD_REQUEST, "키워드가 형식에 맞지 않습니다."),
    KEYWORD_LIMIT_EXCEEDED(BAD_REQUEST, "등록 가능한 키워드 수를 초과하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}