package com.pyonsnalcolor.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    SERVER_UNAVAILABLE(SERVICE_UNAVAILABLE, "서버에 오류가 발생하였습니다."),
    INVALID_PARAMETER(BAD_REQUEST, "입력값이 형식에 맞지 않습니다."),
    INVALID_FILTER_CODE(BAD_REQUEST, "필터 코드값이 유효하지 않습니다."),
    NOT_FOUND_ERROR(NOT_FOUND, "입력값이 형식에 맞지 않습니다."),

    INVALID_PRODUCT_TYPE(BAD_REQUEST, "해당 상품 유형에 상품 id가 존재하지 않습니다."),
    UNMATCHED_PRODUCT_MEMBER(BAD_REQUEST, "찜한 상품 id와 사용자 정보가 일치하지 않습니다."),
    FAVORITE_PRODUCT_ALREADY_EXIST(BAD_REQUEST, "해당하는 찜한 상품이 이미 저장되어 있습니다."),
    FAVORITE_PRODUCT_NOT_EXIST(BAD_REQUEST, "삭제할 상품이 사용자의 찜한 상품에 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
