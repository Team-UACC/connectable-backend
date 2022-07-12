package com.backend.connectable.event.service;

import com.backend.connectable.event.domain.EventRepository;
import com.backend.connectable.event.ui.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<EventResponse> getList() {
        return eventRepository.findAll().stream()
            .map(event -> EventResponse.builder()
                .id(event.getId())
                .name(event.getEventName())
                .image(event.getEventImage())
                .date(event.getStartTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .description(event.getDescription())
                .salesFrom(event.getSalesFrom().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .salesTo(event.getSalesTo().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .build())
            .collect(Collectors.toList());
    }
}
