package com.backend.connectable.admin.service;

import com.backend.connectable.exception.KasException;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.dto.TransactionResponse;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderDetailRepository orderDetailRepository;
    private final KasService kasService;

    @Transactional
    public void orderDetailToPaid(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        orderDetail.paid();
        sendTicket(orderDetail);
    }

    private OrderDetail findOrderDetail(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
            .orElseThrow(() -> new IllegalArgumentException("ID에 대응되는 주문 상세가 없습니다."));
    }

    private void sendTicket(OrderDetail orderDetail) {
        String contractAddress = orderDetail.getContractAddress();
        int tokenId = orderDetail.getTokenId();
        String receiverAddress = orderDetail.getKlaytnAddress();
        try {
            TransactionResponse transactionResponse = kasService.sendMyToken(contractAddress, tokenId, receiverAddress);
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
