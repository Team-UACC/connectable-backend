package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select distinct ticket " +
        "from Ticket as ticket " +
        "join fetch ticket.event as event " +
        "join fetch event.artist as artist " +
        "where ticket.tokenUri in :tokenUris")
    List<Ticket> findAllByTokenUri(@Param("tokenUris") List<String> tokenUris);
}
