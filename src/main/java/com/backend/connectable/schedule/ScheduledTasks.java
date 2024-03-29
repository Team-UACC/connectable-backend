package com.backend.connectable.schedule;

import com.backend.connectable.event.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final TicketRepository ticketRepository;

    @Scheduled(cron = "0 0 10 * * *")
    @Transactional
    public void expireTickets() {
        log.info("##RUNNING##EXPIRE_TICKET_STATUS");
        long fetchedCount = ticketRepository.modifyTicketSalesStatusExpire();
        log.info("##UPDATED##EXPITRE_TICKETS##COUNT::{}", fetchedCount);
    }
}
