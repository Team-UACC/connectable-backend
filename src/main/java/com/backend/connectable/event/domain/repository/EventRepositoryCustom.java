package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTickets;

import java.util.List;

public interface EventRepositoryCustom {

    EventDetail findEventDetailByEventId(Long eventId);

    List<EventTickets> findAllTickets(Long eventId);
}
