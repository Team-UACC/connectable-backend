package com.backend.connectable.exception;

import static com.backend.connectable.exception.ErrorType.UNEXPECTED_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConnectableException.class)
    public ResponseEntity<ExceptionResponse> handleConnectableException(ConnectableException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getBody());
    }

    @ExceptionHandler(KasException.class)
    public ResponseEntity<KasExceptionResponse> handleKasException(KasException e) {
        KasExceptionResponse kasExceptionResponse = e.getKasExceptionResponse();
        log.error("KAS ERROR URL : " + kasExceptionResponse.getUrl());
        log.error("KAS ERROR EXCEPTION MESSAGE : " + kasExceptionResponse.getExceptionMessage());
        return ResponseEntity.internalServerError().body(kasExceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionResponse>
            handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        MethodArgumentNotValidExceptionResponse errorResponse =
                MethodArgumentNotValidExceptionResponse.of(ErrorType.INVALID_REQUEST_ERROR);
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        String unexpectedErrorTrace = ExceptionUtils.getStackTrace(e);
        log.error(unexpectedErrorTrace);
        return ResponseEntity.internalServerError()
                .body(
                        new ExceptionResponse(
                                UNEXPECTED_SERVER_ERROR.getErrorCode(),
                                UNEXPECTED_SERVER_ERROR.getMessage()));
    }
}
