package com.backend.connectable.event.service;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.event.mapper.EventMapper;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.dto.TokenIdentifier;
import com.backend.connectable.kas.service.dto.TokenResponse;
import com.backend.connectable.kas.service.dto.TokensResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final KasService kasService;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public List<EventResponse> getList() {
        List<Event> events = eventRepository.findAllEventWithOrder();
        return events.stream()
                .map(EventMapper.INSTANCE::eventToResponse)
                .collect(Collectors.toList());
    }

    public EventDetailResponse getEventDetail(Long eventId) {
        EventDetail eventDetail =
                eventRepository
                        .findEventDetailByEventId(eventId)
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.EVENT_NOT_EXISTS));
        return EventMapper.INSTANCE.eventDetailToResponse(eventDetail);
    }

    public List<TicketResponse> getTicketList(Long eventId) {
        List<EventTicket> eventTickets = eventRepository.findAllTickets(eventId);
        return eventTickets.stream()
                .map(EventMapper.INSTANCE::ticketToResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketInfo(Long eventId, Long ticketId) {
        EventTicket eventTicket =
                eventRepository
                        .findTicketByEventIdAndTicketId(eventId, ticketId)
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.TICKET_NOT_EXISTS));
        TokenResponse tokenResponse =
                kasService.getToken(eventTicket.getContractAddress(), eventTicket.getTokenId());
        return TicketResponse.builder()
                .id(eventTicket.getId())
                .price(eventTicket.getPrice())
                .artistName(eventTicket.getArtistName())
                .eventDate(eventTicket.getEventDate())
                .eventName(eventTicket.getEventName())
                .ticketSalesStatus(eventTicket.getTicketSalesStatus())
                .tokenId(eventTicket.getTokenId())
                .tokenUri(eventTicket.getTokenUri())
                .isUsed(eventTicket.isUsed())
                .metadata(eventTicket.getMetadata())
                .contractAddress(eventTicket.getContractAddress())
                .ownedBy(tokenResponse.getOwner())
                .build();
    }

    public List<Ticket> findTicketByUserAddress(String userKlaytnAddress) {
        List<String> contractAddresses = eventRepository.findAllContractAddresses();
        Map<String, TokensResponse> userTokens =
                kasService.findAllTokensOfContractAddressesOwnedByUser(
                        contractAddresses, userKlaytnAddress);

        List<TokenIdentifier> tokenIdentifiers =
                userTokens.values().stream()
                        .map(TokensResponse::getTokenIdentifiers)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        List<Ticket> userTickets = new ArrayList<>();
        for (TokenIdentifier tokenIdentifier : tokenIdentifiers) {
            Ticket ticket =
                    ticketRepository.findByTokenIdAndTokenUri(
                            tokenIdentifier.getTokenId(), tokenIdentifier.getTokenUri());
            userTickets.add(ticket);
        }
        return userTickets;
    }

    public Ticket findTicketById(Long ticketId) {
        return ticketRepository
                .findById(ticketId)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.TICKET_NOT_EXISTS));
    }
}
