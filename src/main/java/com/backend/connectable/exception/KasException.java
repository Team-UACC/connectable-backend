package com.backend.connectable.exception;

import lombok.Getter;

@Getter
public class KasException extends RuntimeException {

    private final KasExceptionResponse kasExceptionResponse;

    public KasException(String url, String exceptionMessage) {
        this.kasExceptionResponse = new KasExceptionResponse(url, exceptionMessage);
    }
}
