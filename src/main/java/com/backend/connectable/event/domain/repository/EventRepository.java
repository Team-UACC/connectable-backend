package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    @Query("select event.contractAddress " +
            "from Event as event")
    List<String> findAllContractAddresses();
}
