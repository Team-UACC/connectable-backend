package com.backend.connectable.admin.service;

import com.backend.connectable.admin.ui.dto.EventIssueRequest;
import com.backend.connectable.admin.ui.dto.TokenIssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminOrderService adminOrderService;
    private final AdminIssueService adminIssueService;

    @Transactional
    public void orderDetailToPaid(Long orderDetailId) {
        adminOrderService.orderDetailToPaid(orderDetailId);
    }

    @Transactional
    public void orderDetailToUnpaid(Long orderDetailId) {
        adminOrderService.orderDetailToUnpaid(orderDetailId);
    }

    @Transactional
    public void orderDetailToRefund(Long orderDetailId) {
        adminOrderService.orderDetailToRefund(orderDetailId);
    }

    @Transactional
    public void issueEvent(EventIssueRequest eventIssueRequest) {
        adminIssueService.issueEvent(eventIssueRequest);
    }

    @Transactional
    public void issueTokens(TokenIssueRequest tokenIssueRequest) {
        adminIssueService.issueTokens(tokenIssueRequest);
    }
}
