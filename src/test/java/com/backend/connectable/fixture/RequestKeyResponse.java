package com.backend.connectable.fixture;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RequestKeyResponse {

    private RequestKeyResponse() {}

    private String requestKey;

    private String status;

    private Long expirationTime;

    public String getRequestKey() {
        return requestKey;
    }

    public String getStatus() {
        return status;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }
}
