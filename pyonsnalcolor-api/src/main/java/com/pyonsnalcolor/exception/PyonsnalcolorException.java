package com.pyonsnalcolor.exception;

import com.pyonsnalcolor.exception.model.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PyonsnalcolorException extends RuntimeException {

    private final ErrorCode errorCode;
}