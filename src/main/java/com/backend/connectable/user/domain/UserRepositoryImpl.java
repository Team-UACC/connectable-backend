package com.backend.connectable.user.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
}
