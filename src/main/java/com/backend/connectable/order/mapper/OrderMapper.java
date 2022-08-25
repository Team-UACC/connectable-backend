package com.backend.connectable.order.mapper;

import com.backend.connectable.order.domain.repository.dto.TicketOrderDetail;
import com.backend.connectable.order.ui.dto.OrderDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDetailResponse ticketOrderDetailToResponse(TicketOrderDetail ticketOrderDetail);
}
