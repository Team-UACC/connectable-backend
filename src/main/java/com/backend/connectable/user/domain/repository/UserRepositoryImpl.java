package com.backend.connectable.user.domain.repository;

import com.backend.connectable.user.domain.dto.UserTicket;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.backend.connectable.artist.domain.QArtist.artist;
import static com.backend.connectable.event.domain.QEvent.event;
import static com.backend.connectable.event.domain.QTicket.ticket;
import static com.backend.connectable.user.domain.QUser.user;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public void deleteUser(String klaytnAddress) {
        queryFactory
            .update(user)
            .set(user.isActive, false)
            .where(user.klaytnAddress.eq(klaytnAddress))
            .execute();
    }

    @Transactional
    public void modifyUser(String klaytnAddress, String nickname, String phoneNumber) {
        queryFactory
            .update(user)
            .set(user.nickname, nickname)
            .set(user.phoneNumber, phoneNumber)
            .where(user.klaytnAddress.eq(klaytnAddress))
            .execute();
    }

    @Override
    public List<UserTicket> getOwnTicketsByUser(Long userId) {
        return queryFactory.select(Projections.constructor(
            UserTicket.class,
            ticket.id,
            ticket.price,
            event.startTime.as("eventDate"),
            event.eventName,
            ticket.ticketSalesStatus,
            ticket.tokenId,
            ticket.tokenUri,
            ticket.ticketMetadata,
            event.contractAddress,
            event.id.as("eventId"),
            artist.artistName
            ))
            .from(ticket)
            .leftJoin(user).on(user.id.eq(ticket.user.id))
            .innerJoin(event).on(ticket.event.id.eq(event.id))
            .innerJoin(artist).on(event.artist.id.eq(artist.id))
            .where(user.id.eq(userId))
            .fetch();
    }
}
