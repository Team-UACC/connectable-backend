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

    private String status;
    private String nickname;
    private String phoneNumber;
    private String klaytnAddress;

    public static UserResponse of(User user) {
        return new UserResponse(
                "success",
                user.getNickname(),
                user.getPhoneNumber(),
                user.getKlaytnAddress()
        );
    }
}
