package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
import java.util.List;

import java.util.Optional;

public interface TicketRepositoryCustom {

    long modifyTicketSalesStatusExpire();

    Ticket findOneOnSaleOfEvent(Long eventId);

    List<Ticket> findTicketsOnSaleOfEvent(Long eventId, Long requestedCount);

    Long countTicketsOnSaleOfEvent(Long eventId);

    Optional<TicketMetadata> findMetadataByTokenIdAndTokenUri(int tokenId, String tokenUri);
}
