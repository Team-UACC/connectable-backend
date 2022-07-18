package com.backend.connectable.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConnectableException.class)
    public ResponseEntity<ExceptionResponse> handleConnectableException(ConnectableException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getBody());
    }

    @ExceptionHandler(KasException.class)
    public ResponseEntity<KasExceptionResponse> handleKasException(KasException e) {
        return ResponseEntity.badRequest().body(e.getKasExceptionResponse());
    }
}
