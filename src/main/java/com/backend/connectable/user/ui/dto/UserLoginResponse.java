package com.backend.connectable.user.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginResponse {

    private static final String PREPARED = "prepared";
    private static final String FAILED = "failed";

    private String status;

    public UserLoginResponse(String status) {
        this.status = status;
    }

    public static UserLoginResponse ofPrepared() {
        return new UserLoginResponse(PREPARED);
    }

    public static UserLoginResponse ofFailed() {
        return new UserLoginResponse(FAILED);
    }

    public boolean checkStatusFailed() {
        return FAILED.equals(status);
    }
}
