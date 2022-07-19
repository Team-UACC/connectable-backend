package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserValidationResponse {

    private Boolean available;

    public static UserValidationResponse ofAvailable() {
        return new UserValidationResponse(true);
    }

    public static UserValidationResponse ofUnavailable() {
        return new UserValidationResponse(false);
    }
}
