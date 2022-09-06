package com.backend.connectable.order.domain;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.global.entity.BaseEntity;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = true)
    private String txHash;

    @OneToOne private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Order order;

    @Builder
    public OrderDetail(Long id, OrderStatus orderStatus, String txHash, Ticket ticket) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.txHash = txHash;
        this.ticket = ticket;
    }

    public OrderDetail(OrderStatus orderStatus, String txHash, Ticket ticket) {
        this(null, orderStatus, txHash, ticket);
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderDetails().remove(this);
        }
        this.order = order;
        this.order.getOrderDetails().add(this);
    }

    public void paid() {
        this.orderStatus = orderStatus.toPaid();
    }

    public void unpaid() {
        this.orderStatus = orderStatus.toUnpaid();
        this.ticket.onSale();
    }

    public void refund() {
        this.orderStatus = orderStatus.toRefund();
        this.ticket.onSale();
    }

    public void transferSuccess(String txHash) {
        this.orderStatus = orderStatus.toTransferSuccess();
        this.txHash = txHash;
        this.ticket.soldOut();
    }

    public void transferFail() {
        this.orderStatus = orderStatus.toTransferFail();
    }

    public String getKlaytnAddress() {
        return order.getUser().getKlaytnAddress();
    }

    public int getTokenId() {
        return ticket.getTokenId();
    }

    public String getContractAddress() {
        return ticket.getContractAddress();
    }
}
