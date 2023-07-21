package com.pyonsnalcolor.exception;

import com.pyonsnalcolor.exception.model.ErrorCode;

public class PyonsnalcolorPushException extends PyonsnalcolorException{

    public PyonsnalcolorPushException(ErrorCode errorCode) {
        super(errorCode);
    }
}