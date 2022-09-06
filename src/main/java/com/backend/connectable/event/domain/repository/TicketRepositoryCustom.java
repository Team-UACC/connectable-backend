package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.kas.service.dto.TokenIdentifier;

public interface TicketRepositoryCustom {

    long modifyTicketSalesStatusExpire();

    Ticket findOneOnSaleOfEvent(Long eventId);
}
