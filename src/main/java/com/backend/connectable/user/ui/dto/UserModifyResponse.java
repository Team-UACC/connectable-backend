package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserModifyResponse {

    private String status;

    public static UserModifyResponse ofSuccess() {
        return new UserModifyResponse("success");
    }
}
