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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = true)
    private String txHash;

    @OneToOne
    private Ticket ticket;

    @Builder
    public OrderDetail(Long id, Order order, OrderStatus orderStatus, String txHash, Ticket ticket) {
        this.id = id;
        this.order = order;
        this.orderStatus = orderStatus;
        this.txHash = txHash;
        this.ticket = ticket;
    }
}
