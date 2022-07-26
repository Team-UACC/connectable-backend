package com.backend.connectable.order.service;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.order.domain.Order;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
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

    private final OrderDetailRepository orderDetailRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public OrderResponse registOrder(ConnectableUserDetails userDetails, OrderRequest request) {
        User user = userDetails.getUser();

        Order order = Order.builder()
            .user(user)
            .amount(request.getAmount())
            .ordererName(request.getUserName())
            .ordererPhoneNumber(request.getPhoneNumber())
            .build();

        List<Ticket> tickets = ticketRepository.findAllById(request.getTicketIds());
        List<OrderDetail> orderDetails = request.getTicketIds().stream()
            .map(ticketId -> OrderDetail.builder()
                .order(order)
                .orderStatus(OrderStatus.REQUESTED)
                .txHash(null)
                .ticket(tickets.remove(0))
                .build())
                .collect(Collectors.toList());

        orderDetailRepository.saveAll(orderDetails);
        return OrderResponse.from("true");
    }
}
