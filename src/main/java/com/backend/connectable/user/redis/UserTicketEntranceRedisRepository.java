package com.backend.connectable.user.redis;

import org.springframework.data.repository.CrudRepository;

public interface UserTicketEntranceRedisRepository extends CrudRepository<UserTicketEntrance, String> {
}
