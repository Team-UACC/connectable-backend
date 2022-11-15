package com.backend.connectable.event.domain.repository;

import static com.backend.connectable.event.domain.QEvent.event;
import static com.backend.connectable.event.domain.QTicket.ticket;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TicketRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public long modifyTicketSalesStatusExpire() {
        return queryFactory
                .update(ticket)
                .set(ticket.ticketSalesStatus, TicketSalesStatus.EXPIRED)
                .where(
                        ticket.event.id.in(
                                JPAExpressions.select(event.id)
                                        .from(event)
                                        .where(event.salesTo.before(LocalDateTime.now()))))
                .execute();
    }

    @Override
    public Ticket findOneOnSaleOfEvent(Long eventId) {
        return queryFactory
                .select(ticket)
                .from(ticket)
                .where(
                        ticket.event
                                .id
                                .eq(eventId)
                                .and(ticket.ticketSalesStatus.eq(TicketSalesStatus.ON_SALE)))
                .limit(1)
                .fetchOne();
    }

    @Override
    public Optional<TicketMetadata> findMetadataByTokenIdAndTokenUri(int tokenId, String tokenUri) {
        return Optional.ofNullable(queryFactory
                .select(ticket.ticketMetadata)
                .from(ticket)
                .where(ticket.tokenId.eq(tokenId).and(ticket.tokenUri.eq(tokenUri)))
                .fetchOne());
    }
}
