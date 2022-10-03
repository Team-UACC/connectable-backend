package com.backend.connectable.event.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.s3.service.S3Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventServiceTest {

    private Artist artist1;
    private Event event1;
    private Event event2;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Ticket ticket4;
    private TicketMetadata ticket1Metadata;
    private TicketMetadata ticket2Metadata;
    private TicketMetadata ticket3Metadata;
    private TicketMetadata ticket4Metadata;

    private static final String CONTRACT_ADDRESS = "0xe99540401ef24aba1b7076ea92c94ec38536c6fb";

    @Autowired TicketRepository ticketRepository;

    @Autowired EventRepository eventRepository;

    @Autowired ArtistRepository artistRepository;

    @Autowired EventService eventService;

    @Autowired S3Service s3Service;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();

        artist1 =
                Artist.builder()
                        .bankCompany("NH")
                        .bankAccount("9000000000099")
                        .artistName("빅나티")
                        .email("bignaughty@gmail.com")
                        .password("temptemp1234")
                        .phoneNumber("01012345678")
                        .artistImage("https://image.url")
                        .build();

        event1 =
                Event.builder()
                        .eventName("test1")
                        .eventImage("/connectable-events/image_0xtest.jpeg")
                        .startTime(LocalDateTime.now())
                        .description("description1")
                        .contractAddress(CONTRACT_ADDRESS)
                        .contractName("event1")
                        .salesFrom(LocalDateTime.now())
                        .salesTo(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .artist(artist1)
                        .build();

        event2 =
                Event.builder()
                        .eventName("test2")
                        .eventImage("/connectable-events/image_0xtest.jpeg")
                        .startTime(LocalDateTime.now())
                        .description("description2")
                        .contractAddress(CONTRACT_ADDRESS)
                        .contractName("event2")
                        .salesFrom(LocalDateTime.now())
                        .salesTo(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .artist(artist1)
                        .build();

        ticket1Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/1.json")
                        .toTicketMetadata();
        ticket2Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/2.json")
                        .toTicketMetadata();
        ticket3Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/3.json")
                        .toTicketMetadata();
        ticket4Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/4.json")
                        .toTicketMetadata();

        ticket1 =
                Ticket.builder()
                        .tokenId(1)
                        .tokenUri("tokenUri")
                        .price(10000)
                        .event(event1)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .ticketMetadata(ticket1Metadata)
                        .build();
        ticket2 =
                Ticket.builder()
                        .tokenId(2)
                        .tokenUri("tokenUri")
                        .price(10000)
                        .event(event1)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .ticketMetadata(ticket2Metadata)
                        .build();
        ticket3 =
                Ticket.builder()
                        .tokenId(3)
                        .tokenUri("tokenUri")
                        .price(10000)
                        .event(event1)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .ticketMetadata(ticket3Metadata)
                        .build();
        ticket4 =
                Ticket.builder()
                        .tokenId(3)
                        .tokenUri("tokenUri")
                        .price(10000)
                        .event(event2)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .ticketMetadata(ticket4Metadata)
                        .build();

        artistRepository.save(artist1);
        eventRepository.saveAll(Arrays.asList(event1, event2));
        ticketRepository.saveAll(Arrays.asList(ticket1, ticket2, ticket3, ticket4));
    }

    @DisplayName("이벤트 목록을 여러개 조회한다.")
    @Test
    void getEvents() {
        // when
        List<EventResponse> events = eventService.getList();

        // then
        assertEquals(2L, events.size());
        assertThat(events.get(0).getId()).isEqualTo(event1.getId());
        assertThat(events.get(1).getId()).isEqualTo(event2.getId());
        assertThat(events.get(0).getDescription()).isEqualTo(event1.getDescription());
        assertThat(events.get(1).getDescription()).isEqualTo(event2.getDescription());
    }

    @DisplayName("이벤트 상세를 조회한다.")
    @Test
    void getEventDetail() {
        // when
        EventDetailResponse eventDetailResponse = eventService.getEventDetail(event1.getId());

        // then
        assertThat(eventDetailResponse.getId()).isEqualTo(event1.getId());
        assertThat(eventDetailResponse.getName()).isEqualTo(event1.getEventName());
        assertThat(eventDetailResponse.getContractAddress()).isEqualTo(event1.getContractAddress());
        assertThat(eventDetailResponse.getImage()).isEqualTo(event1.getEventImage());
    }

    @DisplayName("없는 이벤트 상세를 조회시 예외가 발생한다.")
    @Test
    void getEventDetailException() {
        assertThatThrownBy(() -> eventService.getEventDetail(0L))
                .isInstanceOf(ConnectableException.class)
                .hasMessageContaining(ErrorType.EVENT_NOT_EXISTS.getMessage());
    }

    @DisplayName("없는 티켓의 상세 조회시 예외가 발생한다.")
    @Test
    void getTicketInfoException() {
        assertThatThrownBy(() -> eventService.getTicketInfo(event1.getId(), 0L))
                .isInstanceOf(ConnectableException.class)
                .hasMessageContaining(ErrorType.TICKET_NOT_EXISTS.getMessage());
    }

    @DisplayName("특정 이벤트에 귀속된 티켓 목록을 조회한다.")
    @Test
    void getEventTicketList() {
        // when
        List<TicketResponse> ticketResponses = eventService.getTicketList(event1.getId());

        // then
        assertThat(ticketResponses.size() == 3);
        assertThat(ticketResponses.get(0).getTokenId()).isEqualTo(ticket1.getTokenId());
        assertThat(ticketResponses.get(1).getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.ON_SALE);
    }

    @DisplayName("티켓번호로 조회시 티켓 상세를 조회할 수 있다.")
    @Test
    void findTicketById() {
        // when
        Ticket ticket = eventService.findTicketById(ticket1.getId());

        // then
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo(ticket1.getId());
        assertThat(ticket.getPrice()).isEqualTo(ticket1.getPrice());
    }
}
