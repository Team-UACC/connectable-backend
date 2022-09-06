package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryCustom {

    Optional<EventDetail> findEventDetailByEventId(Long eventId);

    List<EventTicket> findAllTickets(Long eventId);

    Optional<EventTicket> findTicketByEventIdAndTicketId(Long eventId, Long ticketId);

    List<Event> findAllEvents();
}
