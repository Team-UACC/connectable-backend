package com.backend.connectable.order.domain.repository;

import com.backend.connectable.order.domain.repository.dto.QTicketOrderDetail;
import com.backend.connectable.order.domain.repository.dto.TicketOrderDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.backend.connectable.event.domain.QTicket.ticket;
import static com.backend.connectable.order.domain.QOrder.order;
import static com.backend.connectable.order.domain.QOrderDetail.orderDetail;
import static com.backend.connectable.user.domain.QUser.user;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<TicketOrderDetail> getOrderDetailList(String klaytnAddress) {
        List<TicketOrderDetail> orderDetailResponses = queryFactory.select(new QTicketOrderDetail(
            ticket.id,
            ticket.ticketSalesStatus,
            order.id,
            orderDetail.id,
            orderDetail.orderStatus,
            orderDetail.modifiedDate,
            orderDetail.txHash
            ))
            .from(order)
            .innerJoin(user).on(order.user.id.eq(user.id))
            .innerJoin(ticket).on(ticket.user.id.eq(user.id))
            .innerJoin(orderDetail).on(orderDetail.order.id.eq(order.id))
            .where(user.klaytnAddress.eq(klaytnAddress))
            .orderBy(orderDetail.modifiedDate.desc())
            .fetch();
        
        return orderDetailResponses;
    }
}
