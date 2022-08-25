package com.backend.connectable.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KasExceptionResponse {
    private String code;
    private String message;
    private String requestId;
}
