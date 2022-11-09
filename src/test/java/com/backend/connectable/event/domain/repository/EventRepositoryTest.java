package com.backend.connectable.event.domain.repository;

import static com.backend.connectable.fixture.ArtistFixture.createArtistBigNaughty;
import static com.backend.connectable.fixture.EventFixture.createEventWithNameAndContractAddress;
import static com.backend.connectable.fixture.EventFixture.createFutureEventWithEventName;
import static com.backend.connectable.fixture.TicketFixture.createTicketWithSalesStatus;
import static com.backend.connectable.fixture.UserFixture.createUserJoel;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class EventRepositoryTest {

    @Autowired EventRepository eventRepository;

    @Autowired TicketRepository ticketRepository;

    @Autowired UserRepository userRepository;

    @Autowired ArtistRepository artistRepository;

    Artist bigNaughty = createArtistBigNaughty();

    User user = createUserJoel();

    String bigNaughtyEvent1ContractAddress = "0x1234";
    Event bigNaughtyEvent1 =
            createEventWithNameAndContractAddress(
                    bigNaughty, "bigNaughty1", bigNaughtyEvent1ContractAddress);

    String bigNaughtyEvent2ContractAddress = "0x5678";
    Event bigNaughtyEvent2 =
            createEventWithNameAndContractAddress(
                    bigNaughty, "bigNaughty2", bigNaughtyEvent2ContractAddress);

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        artistRepository.deleteAll();

        artistRepository.save(bigNaughty);
        eventRepository.saveAll(Arrays.asList(bigNaughtyEvent1, bigNaughtyEvent2));
        userRepository.save(user);
    }

    @DisplayName("이벤트의 컨트랙트 주소를 받아올 수 있다.")
    @Test
    void findAllContractAddresses() {
        List<String> allContractAddresses = eventRepository.findAllContractAddresses();
        assertThat(allContractAddresses)
                .contains(bigNaughtyEvent1ContractAddress, bigNaughtyEvent2ContractAddress);
    }

    @DisplayName("이벤트 목록을 받아올 수 있다.")
    @Test
    void findAllEventWithOrder() {
        List<Event> events = eventRepository.findAllEventWithOrder();
        boolean result =
                Stream.of(bigNaughtyEvent2, bigNaughtyEvent1)
                        .allMatch(
                                item ->
                                        events.stream()
                                                .map(Event::getEventName)
                                                .anyMatch(
                                                        eventName ->
                                                                Objects.equals(
                                                                        eventName,
                                                                        item.getEventName())));
        assertThat(result).isTrue();
    }

    @DisplayName("이벤트의 티켓들을 ON_SALE, PENDING, SOLD_OUT, EXPIRED로 가져오며, 이를 id 순으로 정렬하여 가져올 수 있다.")
    @Test
    void findAllTickets() {
        // given
        Ticket ticket1SoldOut =
                createTicketWithSalesStatus(bigNaughtyEvent1, 1, TicketSalesStatus.SOLD_OUT);
        Ticket ticket2SoldOut =
                createTicketWithSalesStatus(bigNaughtyEvent1, 2, TicketSalesStatus.SOLD_OUT);
        Ticket ticket3Pending =
                createTicketWithSalesStatus(bigNaughtyEvent1, 3, TicketSalesStatus.PENDING);
        Ticket ticket4Pending =
                createTicketWithSalesStatus(bigNaughtyEvent1, 4, TicketSalesStatus.PENDING);
        Ticket ticket5OnSale =
                createTicketWithSalesStatus(bigNaughtyEvent1, 5, TicketSalesStatus.ON_SALE);
        Ticket ticket6OnSale =
                createTicketWithSalesStatus(bigNaughtyEvent1, 6, TicketSalesStatus.ON_SALE);

        ticketRepository.saveAll(
                Arrays.asList(
                        ticket1SoldOut,
                        ticket2SoldOut,
                        ticket3Pending,
                        ticket4Pending,
                        ticket5OnSale,
                        ticket6OnSale));

        // when
        Long eventId = bigNaughtyEvent1.getId();
        List<EventTicket> allTickets = eventRepository.findAllTickets(eventId);

        // then
        assertThat(allTickets.get(0).getTokenId()).isEqualTo(ticket5OnSale.getTokenId());
        assertThat(allTickets.get(1).getTokenId()).isEqualTo(ticket6OnSale.getTokenId());
        assertThat(allTickets.get(2).getTokenId()).isEqualTo(ticket3Pending.getTokenId());
        assertThat(allTickets.get(3).getTokenId()).isEqualTo(ticket4Pending.getTokenId());
        assertThat(allTickets.get(4).getTokenId()).isEqualTo(ticket1SoldOut.getTokenId());
        assertThat(allTickets.get(5).getTokenId()).isEqualTo(ticket2SoldOut.getTokenId());
    }

    @DisplayName("contractAddress로 이벤트를 조회할 수 있다.")
    @Test
    void getEventByContractAddress() {
        // given && when
        Event event = eventRepository.findByContractAddress(bigNaughtyEvent1ContractAddress).get();

        // then
        assertThat(event).isEqualTo(bigNaughtyEvent1);
    }

    @DisplayName("현재 시각보다 판매 종료 시점이 늦은 이벤트를 조회할 수 있다")
    @Test
    void findAllNowAvailable() {
        // given
        Event maxEvent1 = createFutureEventWithEventName(bigNaughty, "maxEvent1");
        Event maxEvent2 = createFutureEventWithEventName(bigNaughty, "maxEvent2");
        eventRepository.saveAll(List.of(maxEvent1, maxEvent2));

        // when
        List<Event> nowAvailable = eventRepository.findAllNowAvailable();

        // then
        assertThat(nowAvailable).containsExactly(maxEvent1, maxEvent2);
    }

    @DisplayName("아티스트 ID를 통해 아티스트의 이벤트를 조회할 수 있다.")
    @Test
    void findAllEventsByArtistId() {
        // given && when
        List<Event> artistEvents = eventRepository.findAllEventsByArtistId(bigNaughty.getId());

        // then
        assertThat(artistEvents).containsExactly(bigNaughtyEvent1, bigNaughtyEvent2);
    }
}
