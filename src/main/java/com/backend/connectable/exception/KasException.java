package com.backend.connectable.exception;

import lombok.Getter;

@Getter
public class KasException extends RuntimeException {

    private final KasExceptionResponse kasExceptionResponse;

    public KasException(KasExceptionResponse kasExceptionResponse) {
        this.kasExceptionResponse = kasExceptionResponse;
    }
}
