package com.backend.connectable.user.redis;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "UserTicketEntrance", timeToLive = 60)
public class UserTicketEntrance {

    @Id private final String klaytnAddress;
    private final Long ticketId;
    private final String verification;

    @Builder
    public UserTicketEntrance(String klaytnAddress, Long ticketId, String verification) {
        this.klaytnAddress = klaytnAddress;
        this.ticketId = ticketId;
        this.verification = verification;
    }
}
