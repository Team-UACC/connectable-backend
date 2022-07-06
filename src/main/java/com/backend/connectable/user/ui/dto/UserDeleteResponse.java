package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDeleteResponse {

    private String status;

    public static UserDeleteResponse ofSuccess() {
        return new UserDeleteResponse("success");
    }
}
