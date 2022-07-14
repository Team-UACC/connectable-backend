package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTickets;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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
    public EventDetail findEventDetailByEventId(Long eventId) {
        return queryFactory.select(Projections.bean(
                EventDetail.class,
                event.id,
                event.eventName,
                event.eventImage,
                artist.artistName,
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
            .fetchOne();
    }

    @Override
    public List<EventTickets> findAllTickets(Long eventId) {
        List<EventTickets> eventTickets = queryFactory.select(Projections.bean(
            EventTickets.class,
            ticket.id,
            ticket.price,
            artist.artistName,
            event.startTime.as("eventDate"),
            event.eventName,
            ticket.onSale,
            ticket.tokenId,
            ticket.tokenUri,
            ticket.ticketMetadata
        ))
            .from(event)
            .innerJoin(ticket).on(ticket.event.id.eq(event.id))
            .innerJoin(artist).on(event.artist.id.eq(artist.id))
            .where(ticket.event.id.eq(eventId))
            .groupBy(ticket.id)
            .fetch();
        return eventTickets;
    }
}
