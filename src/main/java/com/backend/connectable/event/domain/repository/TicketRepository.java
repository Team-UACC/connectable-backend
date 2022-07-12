package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
