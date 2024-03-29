package com.backend.connectable.event.ui;

import com.backend.connectable.event.service.EventService;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getList() {
        List<EventResponse> eventResponses = eventService.getList();
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/today")
    public ResponseEntity<List<EventResponse>> getNowAvailable() {
        List<EventResponse> eventResponses = eventService.getListNowAvailable();
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEventDetail(@PathVariable("id") Long eventId) {
        EventDetailResponse eventDetailResponse = eventService.getEventDetail(eventId);
        return ResponseEntity.ok(eventDetailResponse);
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketResponse>> getTicketList(@PathVariable("id") Long eventId) {
        List<TicketResponse> ticketResponses = eventService.getTicketList(eventId);
        return ResponseEntity.ok(ticketResponses);
    }

    @GetMapping("/{event-id}/tickets/{ticket-id}")
    public ResponseEntity<TicketResponse> getTicketInfo(
            @PathVariable("event-id") Long eventId, @PathVariable("ticket-id") Long ticketId) {
        TicketResponse ticketResponse = eventService.getTicketInfo(eventId, ticketId);
        return ResponseEntity.ok(ticketResponse);
    }
}
