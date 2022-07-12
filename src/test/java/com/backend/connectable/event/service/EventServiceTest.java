package com.backend.connectable.event.service;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.ui.dto.EventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventServiceTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @DisplayName("이벤트 목록을 여러개 조회한다.")
    @Test
    void getEvents() {
        // given
        Event requestEvent1 = Event.builder()
            .eventName("test1")
            .eventImage("/connectable-events/image_0xtest.jpeg")
            .startTime(LocalDateTime.now())
            .description("description1")
            .salesFrom(LocalDateTime.now())
            .salesTo(LocalDateTime.now())
            .build();
        eventRepository.save(requestEvent1);

        Event requestEvent2 = Event.builder()
            .eventName("test2")
            .eventImage("/connectable-events/image_0xtest.jpeg")
            .startTime(LocalDateTime.now())
            .description("description2")
            .salesFrom(LocalDateTime.now())
            .salesTo(LocalDateTime.now())
            .build();
        eventRepository.save(requestEvent2);

        // when
        List<EventResponse> events = eventService.getList();

        // then
        assertEquals(2L, events.size());
    }
}