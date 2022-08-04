package com.backend.connectable.order.domain.repository;

import com.backend.connectable.order.domain.repository.dto.TicketOrderDetail;

import java.util.List;

public interface OrderRepositoryCustom {

    List<TicketOrderDetail> getOrderDetailList(String klaytnAddress);
}
