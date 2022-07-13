package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.ui.dto.EventDetail;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
            .where(ticket.event.id.eq(eventId))
            .groupBy(event.id)
            .limit(1)
            .fetchOne();
    }
}
