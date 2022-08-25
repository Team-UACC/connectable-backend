package com.backend.connectable.order.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {

    private String status;

    public static OrderResponse from(String status) {
        return new OrderResponse(
            status
        );
    }
}
