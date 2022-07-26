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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public OrderResponse registOrder(ConnectableUserDetails userDetails, OrderRequest orderRequest) {
        User user = userDetails.getUser();

        Order order = mapOf(user, orderRequest);
        orderRepository.save(order);
        return OrderResponse.from("true");
    }

    private Order mapOf(User user, OrderRequest orderRequest) {
        List<Ticket> tickets = ticketRepository.findAllById(orderRequest.getTicketIds());
        return new Order(
            user,
            orderRequest.getAmount(),
            orderRequest.getUserName(),
            orderRequest.getPhoneNumber(),
            orderRequest.getTicketIds()
                .stream().map(ticketId -> toOrderDetail(tickets.remove(0)))
                .collect(Collectors.toList()));
    }

    private OrderDetail toOrderDetail(Ticket ticket) {
        return new OrderDetail(
            OrderStatus.REQUESTED,
            null,
            ticket
        );
    }
}
