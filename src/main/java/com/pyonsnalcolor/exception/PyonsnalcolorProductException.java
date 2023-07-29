package com.pyonsnalcolor.exception;

import com.pyonsnalcolor.exception.model.ErrorCode;

public class PyonsnalcolorProductException extends PyonsnalcolorException {

    public PyonsnalcolorProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}