package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.ui.dto.EventDetail;

public interface EventRepositoryCustom {

    EventDetail findEventDetailByEventId(Long eventId);
}
