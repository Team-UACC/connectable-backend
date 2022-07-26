package com.backend.connectable.order.domain;

import com.backend.connectable.global.entity.BaseEntity;
import com.backend.connectable.user.domain.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private int amount;

    private String ordererName;

    private String ordererPhoneNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Builder
    public Order(Long id, User user, int amount, String ordererName, String ordererPhoneNumber, List<OrderDetail> orderDetails) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
        this.orderDetails.addAll(orderDetails);
    }

    public Order(User user, int amount, String ordererName, String ordererPhoneNumber, List<OrderDetail> orderDetails) {
        this.user = user;
        this.amount = amount;
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
        this.orderDetails.addAll(orderDetails);
    }
}
