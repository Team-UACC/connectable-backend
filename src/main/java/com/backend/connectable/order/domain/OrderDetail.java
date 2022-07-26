package com.backend.connectable.order.domain;

import com.backend.connectable.event.domain.Ticket;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private String txHash;

    @OneToOne
    private Ticket ticket;

    @Builder
    public OrderDetail(Long id, Order order, OrderStatus orderStatus, LocalDateTime modifiedDate, String txHash, Ticket ticket) {
        this.id = id;
        this.order = order;
        this.orderStatus = orderStatus;
        this.modifiedDate = modifiedDate;
        this.txHash = txHash;
        this.ticket = ticket;
    }
}
