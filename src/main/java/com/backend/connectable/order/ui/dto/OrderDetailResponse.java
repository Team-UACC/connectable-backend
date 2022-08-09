package com.backend.connectable.order.ui.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.global.common.util.DateTimeUtil;
import com.backend.connectable.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class OrderDetailResponse {

    private Long ticketId;
    private TicketSalesStatus ticketSalesStatus;
    private TicketMetadata ticketMetadata;
    private int price;
    private Long eventId;
    private Long orderId;
    private Long orderDetailId;
    private OrderStatus orderStatus;
    private Long modifiedDate;
    private String txHash;

    @Builder
    public OrderDetailResponse(Long ticketId, TicketSalesStatus ticketSalesStatus, TicketMetadata ticketMetadata, int price, Long eventId, Long orderId, Long orderDetailId, OrderStatus orderStatus, LocalDateTime modifiedDate, String txHash) {
        this.ticketId = ticketId;
        this.ticketSalesStatus = ticketSalesStatus;
        this.ticketMetadata = ticketMetadata;
        this.price = price;
        this.eventId = eventId;
        this.orderId = orderId;
        this.orderDetailId = orderDetailId;
        this.orderStatus = orderStatus;
        this.modifiedDate = DateTimeUtil.toEpochMilliSeconds(modifiedDate);
        this.txHash = txHash;
    }
}
