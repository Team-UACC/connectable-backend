package com.backend.connectable.user.ui.dto;

import com.backend.connectable.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAILURE = "failure";
    private String status;
    private String nickname;
    private String phoneNumber;
    private String klaytnAddress;

    public static UserResponse ofSuccess(User user) {
        return new UserResponse(
                STATUS_SUCCESS, user.getNickname(), user.getPhoneNumber(), user.getKlaytnAddress());
    }

    public static UserResponse ofFailure(User user) {
        return new UserResponse(
                STATUS_FAILURE, user.getNickname(), user.getPhoneNumber(), user.getKlaytnAddress());
    }

    public boolean checkSuccess() {
        return this.status.equals(STATUS_SUCCESS);
    }
}
