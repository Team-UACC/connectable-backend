package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
}
