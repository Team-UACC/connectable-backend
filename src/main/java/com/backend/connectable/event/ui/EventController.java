package com.backend.connectable.event.ui;

import com.backend.connectable.event.service.EventService;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getList() {
        return eventService.getList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEventDetail(@PathVariable("id") Long eventId) {
        EventDetailResponse eventDetailResponse = eventService.getEventDetail(eventId);
        return ResponseEntity.ok(eventDetailResponse);
    }

    @GetMapping("/{id}/tickets")
    public List<TicketResponse> getTicketList(@PathVariable("id") Long eventId) {
        List<TicketResponse> ticketResponse = eventService.getTicketList(eventId);
        return ticketResponse;
    }
}
