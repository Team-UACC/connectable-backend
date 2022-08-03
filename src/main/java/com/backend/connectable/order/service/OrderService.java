package com.backend.connectable.order.service;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.order.domain.Order;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.order.ui.dto.OrderResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public OrderResponse createOrder(ConnectableUserDetails userDetails, OrderRequest orderRequest) {
        User user = userDetails.getUser();
        Order order = generateOrder(user, orderRequest);
        orderRepository.save(order);
        return OrderResponse.from("success");
    }

    private Order generateOrder(User user, OrderRequest orderRequest) {
        List<OrderDetail> orderDetails = generateOrderDetails(orderRequest.getTicketIds());
        Order order = Order.builder()
            .user(user)
            .ordererName(orderRequest.getUserName())
            .ordererPhoneNumber(orderRequest.getPhoneNumber())
            .build();
        order.addOrderDetails(orderDetails);
        return order;
    }

    private List<OrderDetail> generateOrderDetails(List<Long> ticketIds) {
        List<Ticket> tickets = ticketRepository.findAllById(ticketIds);
        tickets.forEach(Ticket::toPending);
        return tickets.stream()
            .map(this::toOrderDetail)
            .collect(Collectors.toList());
    }

    private OrderDetail toOrderDetail(Ticket ticket) {
        return new OrderDetail(
            OrderStatus.REQUESTED,
            null,
            ticket
        );
    }
}
