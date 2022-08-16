package com.backend.connectable.user.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.backend.connectable.user.domain.QUser.user;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void deleteUser(String klaytnAddress) {
        queryFactory
            .update(user)
            .set(user.isActive, false)
            .where(user.klaytnAddress.eq(klaytnAddress))
            .execute();
    }
}
