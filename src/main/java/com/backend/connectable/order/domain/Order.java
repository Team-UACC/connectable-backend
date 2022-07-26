package com.backend.connectable.order.domain;

import com.backend.connectable.global.entity.BaseEntity;
import com.backend.connectable.user.domain.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private int amount;

    private String ordererName;

    private String ordererPhoneNumber;

    @Builder
    public Order(Long id, User user, int amount, String ordererName, String ordererPhoneNumber) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
    }
}
