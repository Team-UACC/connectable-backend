package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Ticket;

public interface TicketRepositoryCustom {

    long modifyTicketSalesStatusExpire();

    Ticket findOneOnSaleOfEvent(Long eventId);
}
