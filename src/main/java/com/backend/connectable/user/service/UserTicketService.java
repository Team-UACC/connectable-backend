package com.backend.connectable.user.service;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.service.EventService;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.global.common.util.RandomStringUtil;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.backend.connectable.user.redis.UserTicketEntrance;
import com.backend.connectable.user.redis.UserTicketEntranceRedisRepository;
import com.backend.connectable.user.ui.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTicketService {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final UserTicketEntranceRedisRepository userTicketEntranceRedisRepository;

    @Value("${entrance.device-secret}")
    private String deviceSecret;

    public UserTicketListResponse getUserTicketsByUserDetails(ConnectableUserDetails userDetails) {
        String userKlaytnAddress = userDetails.getKlaytnAddress();
        List<Ticket> tickets = eventService.findTicketByUserAddress(userKlaytnAddress);
        List<UserTicketResponse> userTicketResponses = UserTicketResponse.toList(tickets);
        return UserTicketListResponse.of(userTicketResponses);
    }

    public UserTicketVerificationResponse generateUserTicketEntranceVerification(User user, Long ticketId) {
        Ticket ticket = eventService.findTicketById(ticketId);
        if (ticket.isUsed()) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ENTRANCE_ALREADY_DONE);
        }

        String klaytnAddress = user.getKlaytnAddress();
        String verification = RandomStringUtil.generate();
        saveUserTicketEntrance(klaytnAddress, ticketId, verification);
        return UserTicketVerificationResponse.of(klaytnAddress, ticketId, verification);
    }

    private void saveUserTicketEntrance(String klaytnAddress, Long ticketId, String verification) {
        UserTicketEntrance userTicketEntrance = UserTicketEntrance.builder()
            .klaytnAddress(klaytnAddress)
            .ticketId(ticketId)
            .verification(verification)
            .build();
        userTicketEntranceRedisRepository.save(userTicketEntrance);
    }

    public UserTicketEntranceResponse useTicketToEnter(Long ticketId, UserTicketEntranceRequest userTicketEntranceRequest) {
        Ticket ticket = eventService.findTicketById(ticketId);
        validateTicketEntrance(ticket, userTicketEntranceRequest);
        User user = userRepository.findByKlaytnAddress(userTicketEntranceRequest.getKlaytnAddress()).get();
        log.info("##USER::{}ENTERED@@CONTACT::{}@@TICKETS::{}", user.getNickname(), user.getPhoneNumber(), ticketId);
        ticket.useToEnter();
        return UserTicketEntranceResponse.ofSuccess();
    }

    private void validateTicketEntrance(Ticket ticket, UserTicketEntranceRequest userTicketEntranceRequest) {
        if (ticket.isUsed()) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ENTRANCE_ALREADY_DONE);
        }

        String deviceSecret = userTicketEntranceRequest.getDeviceSecret();
        if (!this.deviceSecret.equals(deviceSecret)) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ENTRANCE_AUTHORIZATION_NEEDED);
        }

        String klaytnAddress = userTicketEntranceRequest.getKlaytnAddress();
        UserTicketEntrance userTicketEntrance = userTicketEntranceRedisRepository.findById(klaytnAddress)
            .orElseThrow(() -> new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ENTRANCE_INFO_NOT_FOUND));

        if (!Objects.equals(userTicketEntrance.getVerification(), userTicketEntranceRequest.getVerification()) ||
            !Objects.equals(userTicketEntrance.getTicketId(), ticket.getId())) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ENTRANCE_INFO_INVALID);
        }
    }
}
