package com.backend.connectable.event.service;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.ui.dto.EventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventServiceTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EventService eventService;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @DisplayName("이벤트 목록을 여러개 조회한다.")
    @Test
    void getEvents() {
        // given
        Artist artist = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage("https://image.url")
            .build();
        artistRepository.save(artist);

        Event requestEvent1 = Event.builder()
            .eventName("test1")
            .eventImage("/connectable-events/image_0xtest.jpeg")
            .startTime(LocalDateTime.now())
            .description("description1")
            .salesFrom(LocalDateTime.now())
            .salesTo(LocalDateTime.now())
            .artist(artist)
            .build();

        Event requestEvent2 = Event.builder()
            .eventName("test2")
            .eventImage("/connectable-events/image_0xtest.jpeg")
            .startTime(LocalDateTime.now())
            .description("description2")
            .salesFrom(LocalDateTime.now())
            .salesTo(LocalDateTime.now())
            .artist(artist)
            .build();
        eventRepository.saveAll(Arrays.asList(requestEvent1, requestEvent2));

        // when
        List<EventResponse> events = eventService.getList();

        // then
        assertEquals(2L, events.size());
    }
}
