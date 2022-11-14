package com.backend.connectable.event.service;

import static com.backend.connectable.fixture.EventFixture.createFutureEventWithEventName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

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
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.token.dto.TokenIdentifier;
import com.backend.connectable.kas.service.token.dto.TokenResponse;
import com.backend.connectable.kas.service.token.dto.TokensResponse;
import com.backend.connectable.s3.service.S3Service;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class EventServiceTest {

    private Artist artist1;
    private Event event1;
    private Event event2;
    private Event event3;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Ticket ticket4;

    private static final String TOKEN_URI = "tokenUri";
    private static final String CONTRACT_ADDRESS = "0xe99540401ef24aba1b7076ea92c94ec38536c6fb";

    @Autowired TicketRepository ticketRepository;

    @Autowired EventRepository eventRepository;

    @Autowired ArtistRepository artistRepository;

    @Autowired EventService eventService;

    @Autowired S3Service s3Service;

    @MockBean KasService kasService;

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
                        .description("description1")
                        .contractAddress(CONTRACT_ADDRESS)
                        .contractName("event1")
                        .salesFrom(LocalDateTime.now())
                        .salesTo(LocalDateTime.now())
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .artist(artist1)
                        .build();

        event2 =
                Event.builder()
                        .eventName("test2")
                        .eventImage("/connectable-events/image_0xtest.jpeg")
                        .description("description2")
                        .contractAddress(CONTRACT_ADDRESS)
                        .contractName("event2")
                        .salesFrom(LocalDateTime.now())
                        .salesTo(LocalDateTime.now())
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .artist(artist1)
                        .build();

        event3 =
                Event.builder()
                        .eventName("test3")
                        .eventImage("/connectable-events/image_0xtest.jpeg")
                        .description("description3")
                        .contractAddress(CONTRACT_ADDRESS)
                        .contractName("event3")
                        .salesFrom(LocalDateTime.now())
                        .salesTo(LocalDateTime.now())
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .artist(artist1)
                        .build();

        TicketMetadata ticket1Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/1.json")
                        .toTicketMetadata();
        TicketMetadata ticket2Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/2.json")
                        .toTicketMetadata();
        TicketMetadata ticket3Metadata =
                s3Service
                        .fetchMetadata(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/3.json")
                        .toTicketMetadata();
        TicketMetadata ticket4Metadata =
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
        eventRepository.saveAll(Arrays.asList(event1, event2, event3));
        ticketRepository.saveAll(Arrays.asList(ticket1, ticket2, ticket3, ticket4));

        TokenResponse tokenResponse =
                new TokenResponse("0x1234", "0x5678", "0x1", TOKEN_URI, "0xwelcome");
        TokensResponse tokensResponse = new TokensResponse("eyJjm...ZSJ9", List.of(tokenResponse));
        List<TokenIdentifier> tokenIdentifiers =
                new ArrayList<>(tokensResponse.getTokenIdentifiers());

        given(kasService.getToken(any(String.class), any(Integer.class))).willReturn(tokenResponse);
        given(kasService.findAllTokensOwnedByUser(anyList(), any(String.class)))
                .willReturn(tokenIdentifiers);
    }

    @DisplayName("이벤트 목록을 여러개 조회한다.")
    @Test
    void getEvents() {
        // when
        List<EventResponse> events = eventService.getList();

        // then
        assertEquals(3L, events.size());
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
        assertThat(eventDetailResponse.getArtistId()).isEqualTo(event1.getArtist().getId());
    }

    @DisplayName("종속된 티켓이 없는 이벤트의 상세 조회에 성공한다.")
    @Test
    void getEventDetailWithoutTickets() {
        // when
        EventDetailResponse eventDetailResponse = eventService.getEventDetail(event3.getId());

        // then
        assertThat(eventDetailResponse.getId()).isEqualTo(event3.getId());
        assertThat(eventDetailResponse.getName()).isEqualTo(event3.getEventName());
        assertThat(eventDetailResponse.getArtistId()).isEqualTo(event3.getArtist().getId());
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

    @DisplayName("티켓의 정보를 받아올 수 있다.")
    @Test
    void getTicketInfo() {
        // when
        TicketResponse ticketResponse =
                eventService.getTicketInfo(ticket1.getEventId(), ticket1.getId());

        // then
        assertThat(ticketResponse).isNotNull();
        assertThat(ticketResponse.getArtistName()).isEqualTo(ticket1.getArtistName());
        assertThat(ticketResponse.getTokenId()).isEqualTo(ticket1.getTokenId());
        assertThat(ticketResponse.getEventName()).isEqualTo(ticket1.getEvent().getEventName());
    }

    @DisplayName("개인의 지갑주소로 소유 티켓목록을 조회할 수 있다.")
    @Test
    void findTicketByUserAddress() {
        // given & when
        String userKlaytnAddress = "0x1234";
        List<Ticket> tickets = eventService.findTicketByUserAddress(userKlaytnAddress);

        assertThat(tickets.get(0).getTokenUri()).isEqualTo(TOKEN_URI);
        assertThat(tickets.get(0).getPrice()).isEqualTo(10000);
    }

    @DisplayName("현재 구매할 수 있는 이벤트를 조회할 수 있다.")
    @Test
    void getListNowAvailable() {
        // before
        List<EventResponse> before = eventService.getListNowAvailable();
        assertThat(before).isEmpty();

        // given
        Event maxEvent1 = createFutureEventWithEventName(artist1, "maxEvent1");
        Event maxEvent2 = createFutureEventWithEventName(artist1, "maxEvent2");
        eventRepository.saveAll(List.of(maxEvent1, maxEvent2));

        // when
        List<EventResponse> after = eventService.getListNowAvailable();

        // then
        assertThat(after).hasSize(2);
    }
}
