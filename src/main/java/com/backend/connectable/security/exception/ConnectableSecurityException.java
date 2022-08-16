package com.backend.connectable.security.exception;

import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectableSecurityException extends Exception {

    private static final ObjectMapper JSON_CONVERTER = new ObjectMapper();

    public ConnectableSecurityException(String message) {
        super(message);
    }

    public ConnectableSecurityException(ErrorType errorType) {
        this(generateExceptionMessage(errorType));
    }

    private static String generateExceptionMessage(ErrorType errorType) {
        try {
            return JSON_CONVERTER.writeValueAsString(new ExceptionResponse(errorType.getErrorCode(), errorType.getMessage()));
        } catch (JsonProcessingException e) {
            return "JWT 인증 오류";
        }
    }
}
