package com.backend.connectable.user.ui.dto;

import com.backend.connectable.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginSuccessResponse extends UserLoginResponse {

    private String klaytnAddress;
    private String jwt;
    private Boolean isNew;

    public UserLoginSuccessResponse(String status, String klaytnAddress, String jwt, Boolean isNew) {
        super(status);
        this.klaytnAddress = klaytnAddress;
        this.jwt = jwt;
        this.isNew = isNew;
    }

    public static UserLoginSuccessResponse of(User user, String jwtToken, Boolean isNew) {
        return new UserLoginSuccessResponse(
                "completed",
                user.getKlaytnAddress(),
                jwtToken,
                isNew
        );
    }
}
