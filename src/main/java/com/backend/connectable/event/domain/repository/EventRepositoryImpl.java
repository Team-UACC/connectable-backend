package com.backend.connectable.event.domain.repository;

import static com.backend.connectable.artist.domain.QArtist.artist;
import static com.backend.connectable.event.domain.QEvent.event;
import static com.backend.connectable.event.domain.QTicket.ticket;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.event.domain.dto.QEventTicket;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EventRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<EventDetail> findEventDetailByEventId(Long eventId) {
        // ToDo Event에 매핑된 Ticket이 없는 경우는 EVENT-001 Exception 발생
        EventDetail result =
                queryFactory
                        .select(
                                Projections.bean(
                                        EventDetail.class,
                                        event.id,
                                        event.eventName,
                                        event.eventImage,
                                        artist.artistName,
                                        artist.artistImage,
                                        event.startTime,
                                        event.endTime,
                                        event.description,
                                        event.contractAddress,
                                        event.contractName,
                                        event.salesFrom,
                                        event.salesTo,
                                        event.twitterUrl,
                                        event.instagramUrl,
                                        event.webpageUrl,
                                        ticket.count().intValue().as("totalTicketCount"),
                                        ExpressionUtils.as(
                                                JPAExpressions.select(ticket.count().intValue())
                                                        .from(ticket)
                                                        .where(
                                                                ticket.ticketSalesStatus
                                                                        .eq(
                                                                                TicketSalesStatus
                                                                                        .ON_SALE)
                                                                        .and(
                                                                                ticket.event.id.eq(
                                                                                        eventId))),
                                                "onSaleTicketCount"),
                                        ticket.price,
                                        event.location,
                                        event.salesOption))
                        .from(event)
                        .leftJoin(ticket)
                        .on(ticket.event.id.eq(event.id))
                        .innerJoin(artist)
                        .on(event.artist.id.eq(artist.id))
                        .where(event.id.eq(eventId))
                        .groupBy(event.id)
                        .limit(1)
                        .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<EventTicket> findAllTickets(Long eventId) {
        List<EventTicket> result =
                queryFactory
                        .select(
                                new QEventTicket(
                                        ticket.id,
                                        ticket.price,
                                        artist.artistName,
                                        event.startTime.as("eventDate"),
                                        event.eventName,
                                        ticket.ticketSalesStatus,
                                        ticket.tokenId,
                                        ticket.tokenUri,
                                        ticket.isUsed,
                                        ticket.ticketMetadata,
                                        event.contractAddress.as("contractAddress")))
                        .from(event)
                        .innerJoin(ticket)
                        .on(ticket.event.id.eq(event.id))
                        .innerJoin(artist)
                        .on(event.artist.id.eq(artist.id))
                        .where(ticket.event.id.eq(eventId))
                        .groupBy(ticket.id)
                        .orderBy(ticketSortSpecifier(), ticket.id.asc())
                        .fetch();

        return result;
    }

    @Override
    public Optional<EventTicket> findTicketByEventIdAndTicketId(Long eventId, Long ticketId) {
        EventTicket result =
                queryFactory
                        .select(
                                new QEventTicket(
                                        ticket.id,
                                        ticket.price,
                                        artist.artistName,
                                        event.startTime.as("eventDate"),
                                        event.eventName,
                                        ticket.ticketSalesStatus,
                                        ticket.tokenId,
                                        ticket.tokenUri,
                                        ticket.isUsed,
                                        ticket.ticketMetadata,
                                        event.contractAddress))
                        .from(event)
                        .innerJoin(ticket)
                        .on(ticket.event.id.eq(event.id))
                        .innerJoin(artist)
                        .on(event.artist.id.eq(artist.id))
                        .where(ticket.event.id.eq(eventId).and(ticket.id.eq(ticketId)))
                        .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Event> findAllEventWithOrder() {
        List<Event> result =
                queryFactory
                        .selectFrom(event)
                        .orderBy(eventSortSpecifier(), event.salesTo.asc())
                        .fetch();

        return result;
    }

    private OrderSpecifier<Integer> eventSortSpecifier() {
        NumberExpression<Integer> cases =
                new CaseBuilder()
                        .when(event.salesTo.after(LocalDateTime.now()))
                        .then(1)
                        .otherwise(2);

        return new OrderSpecifier<>(Order.ASC, cases);
    }

    private OrderSpecifier<Integer> ticketSortSpecifier() {
        NumberExpression<Integer> cases =
                new CaseBuilder()
                        .when(ticket.ticketSalesStatus.eq(TicketSalesStatus.ON_SALE))
                        .then(1)
                        .when(ticket.ticketSalesStatus.eq(TicketSalesStatus.PENDING))
                        .then(2)
                        .when(ticket.ticketSalesStatus.eq(TicketSalesStatus.SOLD_OUT))
                        .then(3)
                        .when(ticket.ticketSalesStatus.eq(TicketSalesStatus.EXPIRED))
                        .then(4)
                        .otherwise(5);

        return new OrderSpecifier<>(Order.ASC, cases);
    }
}
