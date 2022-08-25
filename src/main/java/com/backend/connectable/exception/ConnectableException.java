package com.backend.connectable.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ConnectableException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ExceptionResponse body;

    public ConnectableException(HttpStatus httpStatus, ErrorType errorType) {
        super(errorType.getMessage());
        this.httpStatus = httpStatus;
        this.body = new ExceptionResponse(errorType.getErrorCode(), errorType.getMessage());
    }
}
