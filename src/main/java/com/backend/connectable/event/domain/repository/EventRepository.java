package com.backend.connectable.event.domain.repository;

import com.backend.connectable.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    @Query("select event.contractAddress " +
            "from Event as event")
    List<String> findAllContractAddresses();

    Optional<Event> findByContractAddress(String contractAddress);
}
