package com.backend.connectable.event.service;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.mapper.EventMapper;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final KasService kasService;
    private final EventRepository eventRepository;

    public List<EventResponse> getList() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
            .map(EventMapper.INSTANCE::eventToResponse)
            .collect(Collectors.toList());
    }

    public EventDetailResponse getEventDetail(Long eventId) {
        EventDetail eventDetail = eventRepository.findEventDetailByEventId(eventId)
            .orElseThrow(() -> new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.EVENT_NOT_EXISTS));
        return EventMapper.INSTANCE.eventDetailToResponse(eventDetail);
    }

    public List<TicketResponse> getTicketList(Long eventId) {
        List<EventTicket> eventTickets = eventRepository.findAllTickets(eventId);
        return eventTickets.stream()
            .map(EventMapper.INSTANCE::ticketToResponse)
            .collect(Collectors.toList());
    }

    public TicketResponse getTicketInfo(Long eventId, Long ticketId) {
        EventTicket eventTicket = eventRepository.findTicketByEventIdAndTicketId(eventId, ticketId)
            .orElseThrow(() -> new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.TICKET_NOT_EXISTS));
        TokenResponse tokenResponse = kasService.getToken(eventTicket.getContractAddress(), eventTicket.getTokenId());
        return TicketResponse.builder()
            .id(eventTicket.getId())
            .price(eventTicket.getPrice())
            .artistName(eventTicket.getArtistName())
            .eventDate(eventTicket.getEventDate())
            .eventName(eventTicket.getEventName())
            .ticketSalesStatus(eventTicket.getTicketSalesStatus())
            .tokenId(eventTicket.getTokenId())
            .tokenUri(eventTicket.getTokenUri())
            .metadata(eventTicket.getMetadata())
            .contractAddress(eventTicket.getContractAddress())
            .ownedBy(tokenResponse.getOwner())
            .build();
    }
}
