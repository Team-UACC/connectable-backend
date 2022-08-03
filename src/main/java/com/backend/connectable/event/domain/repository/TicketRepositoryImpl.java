package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.TicketSalesStatus;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static com.backend.connectable.event.domain.QTicket.ticket;
import static com.backend.connectable.event.domain.QEvent.event;

@Repository
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TicketRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public long modifyTicketSalesStatusExpire() {
        long fetchedRowCount = queryFactory
            .update(ticket)
            .set(ticket.ticketSalesStatus, TicketSalesStatus.EXPIRED)
            .where(ticket.event.id.in(JPAExpressions.select(event.id).from(event).where(event.salesTo.before(LocalDateTime.now()))))
            .execute();

        return fetchedRowCount;
    }
}
