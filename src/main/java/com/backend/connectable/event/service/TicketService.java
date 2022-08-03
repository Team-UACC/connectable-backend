package com.backend.connectable.event.service;

import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.event.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;

    @Scheduled(cron = "0 0 10 * * *")
    public void expireTickets() {
        log.info("##RUNNING##EXPIRE_TICKET_STATUS");
        long fetchedCount = ticketRepository.modifyTicketSalesStatusExpire();
        log.info("##UPDATED##EXPITRE_TICKETS##COUNT::{}", fetchedCount);
    }
}
