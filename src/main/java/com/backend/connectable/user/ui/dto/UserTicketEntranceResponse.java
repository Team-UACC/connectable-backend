package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTicketEntranceResponse {

    private static final String SUCCESS = "success";

    private String status;

    public static UserTicketEntranceResponse ofSuccess() {
        return new UserTicketEntranceResponse(SUCCESS);
    }
}
