package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;

public interface TicketRepositoryCustom {

    long modifyTicketSalesStatusExpire();

    Ticket findOneOnSaleOfEvent(Long eventId);

    TicketMetadata findMetadataByTokenIdAndTokenUri(int tokenId, String tokenUri);
}
