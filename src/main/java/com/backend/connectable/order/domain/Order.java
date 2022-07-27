package com.backend.connectable.order.domain;

import com.backend.connectable.global.entity.BaseEntity;
import com.backend.connectable.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private String ordererName;

    private String ordererPhoneNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderDetail> orderDetails = new ArrayList<>();

    @Builder
    public Order(Long id, User user, String ordererName, String ordererPhoneNumber) {
        this.id = id;
        this.user = user;
        this.ordererName = ordererName;
        this.ordererPhoneNumber = ordererPhoneNumber;
    }

    public void addOrderDetails(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(this);
        }
    }
}
