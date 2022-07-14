package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;

import java.util.List;

public interface EventRepositoryCustom {

    EventDetail findEventDetailByEventId(Long eventId);

    List<EventTicket> findAllTickets(Long eventId);

    EventTicket findTicketByEventIdAndTicketId(Long eventId, Long ticketId);
}
