package com.pyonsnalcolor.exception;

import com.pyonsnalcolor.exception.model.ErrorCode;

public class PyonsnalcolorAuthException extends PyonsnalcolorException {

    public PyonsnalcolorAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}