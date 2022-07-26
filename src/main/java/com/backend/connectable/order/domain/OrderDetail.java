package com.backend.connectable.order.domain;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.global.entity.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = true)
    private String txHash;

    @OneToOne
    private Ticket ticket;

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
}
