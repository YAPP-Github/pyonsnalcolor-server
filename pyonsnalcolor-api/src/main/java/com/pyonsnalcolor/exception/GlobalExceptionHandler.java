package com.pyonsnalcolor.exception;

import com.pyonsnalcolor.exception.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PyonsnalcolorAuthException.class)
    public ResponseEntity<Object> handleAuthException(PyonsnalcolorAuthException e) {
        log.error("GlobalExceptionHandler catch PyonsnalcolorAuthException: {}", e.getErrorCode().name());
        ErrorCode errorCode = e.getErrorCode();
        return createResponseEntity(errorCode);
    }

    private ResponseEntity<Object> createResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(createErrorResponse(errorCode));
    }

    private ErrorResponse createErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}