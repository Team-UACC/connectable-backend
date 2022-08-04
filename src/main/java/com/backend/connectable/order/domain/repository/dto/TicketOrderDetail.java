package com.backend.connectable.order.domain.repository.dto;

import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.order.domain.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TicketOrderDetail {

    private Long ticketId;
    private TicketSalesStatus ticketSalesStatus;
    private Long orderId;
    private Long orderDetailId;
    private OrderStatus orderStatus;
    private LocalDateTime modifiedDate;
    private String txHash;

    @QueryProjection

    public TicketOrderDetail(Long ticketId, TicketSalesStatus ticketSalesStatus, Long orderId, Long orderDetailId, OrderStatus orderStatus, LocalDateTime modifiedDate, String txHash) {
        this.ticketId = ticketId;
        this.ticketSalesStatus = ticketSalesStatus;
        this.orderId = orderId;
        this.orderDetailId = orderDetailId;
        this.orderStatus = orderStatus;
        this.modifiedDate = modifiedDate;
        this.txHash = txHash;
    }
}