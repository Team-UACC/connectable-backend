package com.backend.connectable.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTicketListResponse {

    private String status;
    private List<UserTicketResponse> tickets;

    public static UserTicketListResponse of(List<UserTicketResponse> tickets) {
        return new UserTicketListResponse(
            "success",
            tickets
        );
    }
}
