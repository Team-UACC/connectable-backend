package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.event.domain.dto.QEventTicket;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.backend.connectable.artist.domain.QArtist.artist;
import static com.backend.connectable.event.domain.QEvent.event;
import static com.backend.connectable.event.domain.QTicket.ticket;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EventRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<EventDetail> findEventDetailByEventId(Long eventId) {
        return Optional.ofNullable(queryFactory.select(Projections.bean(
            EventDetail.class,
            event.id,
            event.eventName,
            event.eventImage,
            artist.artistName,
            artist.artistImage,
            event.startTime,
            event.endTime,
            event.description,
            event.salesFrom,
            event.salesTo,
            event.twitterUrl,
            event.instagramUrl,
            event.webpageUrl,
            ticket.count().intValue().as("totalTicketCount"),
            ExpressionUtils.as(
                JPAExpressions.select(ticket.count().intValue()).from(ticket).where(ticket.onSale.eq(true))
            , "onSaleTicketCount"),
            ticket.price,
            event.location,
            event.salesOption
            ))
            .from(event)
            .innerJoin(ticket).on(ticket.event.id.eq(event.id))
            .innerJoin(artist).on(event.artist.id.eq(artist.id))
            .where(ticket.event.id.eq(eventId))
            .groupBy(event.id)
            .limit(1)
            .fetchOne()
        );
    }

    @Override
    public List<EventTicket> findAllTickets(Long eventId) {
        return queryFactory.select(new QEventTicket(
            ticket.id,
            ticket.price,
            artist.artistName,
            event.startTime.as("eventDate"),
            event.eventName,
            ticket.onSale,
            ticket.tokenId,
            ticket.tokenUri,
            ticket.ticketMetadata,
            event.contractAddress.as("contractAddress")
            ))
            .from(event)
            .innerJoin(ticket).on(ticket.event.id.eq(event.id))
            .innerJoin(artist).on(event.artist.id.eq(artist.id))
            .where(ticket.event.id.eq(eventId))
            .groupBy(ticket.id)
            .fetch();
    }

    @Override
    public Optional<EventTicket> findTicketByEventIdAndTicketId(Long eventId, Long ticketId) {
        return Optional.ofNullable(queryFactory.select(new QEventTicket(
            ticket.id,
            ticket.price,
            artist.artistName,
            event.startTime.as("eventDate"),
            event.eventName,
            ticket.onSale,
            ticket.tokenId,
            ticket.tokenUri,
            ticket.ticketMetadata,
            event.contractAddress
            ))
            .from(event)
            .innerJoin(ticket).on(ticket.event.id.eq(event.id))
            .innerJoin(artist).on(event.artist.id.eq(artist.id))
            .where(ticket.event.id.eq(eventId)
                .and(ticket.id.eq(ticketId)))
            .fetchOne()
        );
    }
}
