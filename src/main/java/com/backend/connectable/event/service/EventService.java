package com.backend.connectable.event.service;

import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.ui.dto.EventDetail;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .date(event.getStartTime())
                .description(event.getDescription())
                .salesFrom(event.getSalesFrom())
                .salesTo(event.getSalesTo())
                .build())
            .collect(Collectors.toList());
    }

    public EventDetailResponse getEventDetail(Long eventId) {
        EventDetail eventDetail = eventRepository.findEventDetailByEventId(eventId);
        EventDetailResponse result = EventDetailResponse.builder()
                .id(eventDetail.getId())
                .name(eventDetail.getEventName())
                .image(eventDetail.getEventImage())
                .date(eventDetail.getEndTime())
                .description(eventDetail.getDescription())
                .salesFrom(eventDetail.getSalesFrom())
                .salesTo(eventDetail.getSalesTo())
                .twitterUrl(eventDetail.getTwitterUrl())
                .instagramUrl(eventDetail.getInstagramUrl())
                .webpageUrl(eventDetail.getWebpageUrl())
                .totalTicketCount(eventDetail.getTotalTicketCount())
                .onSaleTicketCount(eventDetail.getOnSaleTicketCount())
                .startTime(eventDetail.getStartTime())
                .endTime(eventDetail.getEndTime())
                .price(eventDetail.getPrice())
                .location(eventDetail.getLocation())
                .salesOption(eventDetail.getSalesOption())
                .build();

        return result;
    }
}
