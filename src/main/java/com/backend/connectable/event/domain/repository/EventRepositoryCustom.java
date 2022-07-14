package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.dto.EventDetail;

public interface EventRepositoryCustom {

    EventDetail findEventDetailByEventId(Long eventId);
}
