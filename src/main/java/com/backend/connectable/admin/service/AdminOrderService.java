package com.backend.connectable.admin.service;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.exception.KasException;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.order.domain.Order;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderService {

    private final SmsService smsService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final KasService kasService;

    @Transactional
    public void orderDetailToPaid(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        Order order = findOrderByOrderDetail(orderDetail);
        orderDetail.paid();
        sendTicket(orderDetail);
        smsService.sendPaidNotification(order.getOrdererPhoneNumber());
    }

    private OrderDetail findOrderDetail(Long orderDetailId) {
        return orderDetailRepository
                .findById(orderDetailId)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.ORDER_DETAIL_NOT_EXISTS));
    }

    private Order findOrderByOrderDetail(OrderDetail orderDetail) {
        Long orderId = orderDetail.getOrder().getId();
        return orderRepository.getReferenceById(orderId);
    }

    private void sendTicket(OrderDetail orderDetail) {
        String contractAddress = orderDetail.getContractAddress();
        int tokenId = orderDetail.getTokenId();
        String receiverAddress = orderDetail.getKlaytnAddress();
        try {
            TransactionResponse transactionResponse =
                    kasService.sendMyToken(contractAddress, tokenId, receiverAddress);
            orderDetail.transferSuccess(transactionResponse.getTransactionHash());
        } catch (KasException kasException) {
            orderDetail.transferFail();
        }
    }

    @Transactional
    public void orderDetailToUnpaid(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        orderDetail.unpaid();
    }

    @Transactional
    public void orderDetailToRefund(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        orderDetail.refund();
    }
}
