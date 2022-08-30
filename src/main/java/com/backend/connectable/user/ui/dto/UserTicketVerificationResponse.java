package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTicketVerificationResponse {

    private String klaytnAddress;
    private Long ticketId;
    private String verification;

    public static UserTicketVerificationResponse of(String klaytnAddress, Long ticketId, String verification) {
        return new UserTicketVerificationResponse(
            klaytnAddress,
            ticketId,
            verification
        );
    }
}
