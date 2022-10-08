package com.backend.connectable.order.service;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.order.domain.Order;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.order.domain.repository.dto.TicketOrderDetail;
import com.backend.connectable.order.mapper.OrderMapper;
import com.backend.connectable.order.ui.dto.OrderDetailResponse;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.order.ui.dto.OrderResponse;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public OrderResponse createOrder(
            ConnectableUserDetails userDetails, OrderRequest orderRequest) {
        User user = findUser(userDetails.getKlaytnAddress());
        Order order = generateOrder(user, orderRequest);
        orderRepository.save(order);
        return OrderResponse.from("success");
    }

    public List<OrderDetailResponse> getOrderDetailList(ConnectableUserDetails userDetails) {
        String klaytnAddress = userDetails.getKlaytnAddress();
        List<TicketOrderDetail> orderDetailResponses =
                orderRepository.getOrderDetailList(klaytnAddress);

        return orderDetailResponses.stream()
                .map(OrderMapper.INSTANCE::ticketOrderDetailToResponse)
                .collect(Collectors.toList());
    }

    private User findUser(String klaytnAddress) {
        return userRepository
                .findByKlaytnAddress(klaytnAddress)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.USER_NOT_FOUND));
    }

    private Order generateOrder(User user, OrderRequest orderRequest) {
        List<OrderDetail> orderDetails =
                generateOrderDetails(orderRequest.getTicketIds(), orderRequest.getEventId());
        Order order =
                Order.builder()
                        .user(user)
                        .ordererName(orderRequest.getUserName())
                        .ordererPhoneNumber(orderRequest.getPhoneNumber())
                        .build();
        order.addOrderDetails(orderDetails);
        return order;
    }

    private List<OrderDetail> generateOrderDetails(List<Long> ticketIds, Long eventId) {
        if (checkRandomTicketSelection(ticketIds)) {
            return generateRandomTicketOrderDetail(eventId);
        }

        List<Ticket> tickets = ticketRepository.findAllById(ticketIds);
        tickets.forEach(Ticket::toPending);
        return tickets.stream().map(this::toOrderDetail).collect(Collectors.toList());
    }

    private boolean checkRandomTicketSelection(List<Long> ticketIds) {
        return ticketIds.contains(0L) && ticketIds.size() == 1;
    }

    private List<OrderDetail> generateRandomTicketOrderDetail(Long eventId) {
        Ticket ticket = ticketRepository.findOneOnSaleOfEvent(eventId);
        ticket.toPending();
        OrderDetail orderDetail = toOrderDetail(ticket);
        return List.of(orderDetail);
    }

    private OrderDetail toOrderDetail(Ticket ticket) {
        return new OrderDetail(OrderStatus.REQUESTED, null, ticket);
    }
}
