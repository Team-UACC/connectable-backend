package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public boolean isFailed() {
        return FAILED.equals(status);
    }
}
