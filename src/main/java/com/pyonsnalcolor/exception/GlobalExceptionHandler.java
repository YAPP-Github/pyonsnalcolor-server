package com.pyonsnalcolor.exception;

import com.pyonsnalcolor.exception.model.CommonErrorCode;
import com.pyonsnalcolor.exception.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

import static com.pyonsnalcolor.exception.model.CommonErrorCode.INVALID_PARAMETER;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("GlobalExceptionHandler catch Exception ", e);
        ErrorCode errorCode = CommonErrorCode.SERVER_UNAVAILABLE;
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(PyonsnalcolorAuthException.class)
    public ResponseEntity<Object> handleAuthException(PyonsnalcolorAuthException e) {
        log.error("GlobalExceptionHandler catch PyonsnalcolorAuthException: {}", e.getErrorCode().name());
        ErrorCode errorCode = e.getErrorCode();
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(PyonsnalcolorPushException.class)
    public ResponseEntity<Object> handlePushException(PyonsnalcolorPushException e) {
        log.error("GlobalExceptionHandler catch PyonsnalcolorPushException: {}", e.getErrorCode().name());
        ErrorCode errorCode = e.getErrorCode();
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(PyonsnalcolorProductException.class)
    public ResponseEntity<Object> handleProductException(PyonsnalcolorProductException e) {
        log.error("GlobalExceptionHandler catch PyonsnalcolorProductException: {}", e.getErrorCode().name());
        ErrorCode errorCode = e.getErrorCode();
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e) {
        log.error("GlobalExceptionHandler catch NoSuchElementException", e);
        ErrorCode errorCode = CommonErrorCode.NOT_FOUND_ERROR;

        return createResponseEntity(errorCode);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.error("GlobalExceptionHandler catch MethodArgumentNotValidException: {}", e.getMessage());
        ErrorResponse errorResponse = createErrorResponse(e.getBindingResult());
        return new ResponseEntity<Object>(errorResponse, INVALID_PARAMETER.getHttpStatus());
    }

    private ErrorResponse createErrorResponse(BindingResult bindingResult){
        String errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        return new ErrorResponse(INVALID_PARAMETER.name(), errorMessage);
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
