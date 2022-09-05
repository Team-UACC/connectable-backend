package com.backend.connectable.event.domain.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.SalesOption;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    Artist bigNaughty =
            Artist.builder()
                    .bankCompany("NH")
                    .bankAccount("9000000000099")
                    .artistName("빅나티")
                    .email("bignaughty@gmail.com")
                    .password("temptemp1234")
                    .phoneNumber("01012345678")
                    .artistImage("ARTIST_IMAGE")
                    .build();

    User user =
            User.builder()
                    .phoneNumber("010-0000-0000")
                    .klaytnAddress("0x1234")
                    .nickname("user")
                    .privacyAgreement(true)
                    .isActive(true)
                    .build();

    String joelEventContractAddress = "0x1234";
    Event joelEvent =
            Event.builder()
                    .description("조엘의 콘서트 at Connectable")
                    .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
                    .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
                    .contractAddress(joelEventContractAddress)
                    .eventName("조엘의 콘서트")
                    .eventImage("JOEL_EVENT_IMG_URL")
                    .twitterUrl("https://github.com/joelonsw")
                    .instagramUrl("https://www.instagram.com/jyoung_with/")
                    .webpageUrl("https://papimon.tistory.com/")
                    .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
                    .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
                    .salesOption(SalesOption.FLAT_PRICE)
                    .location("서울특별시 강남구 테헤란로 311 아남타워빌딩 7층")
                    .artist(bigNaughty)
                    .build();

    String ryanEventContractAddress = "0x5678";
    Event ryanEvent =
            Event.builder()
                    .description("라이언 콘서트 at Connectable")
                    .salesFrom(LocalDateTime.of(2022, 6, 11, 0, 0))
                    .salesTo(LocalDateTime.of(2022, 6, 17, 0, 0))
                    .contractAddress(ryanEventContractAddress)
                    .eventName("라이언 콘서트")
                    .eventImage("RYAN_EVENT_IMG_URL")
                    .twitterUrl("https://twitter.com/elonmusk")
                    .instagramUrl("https://www.instagram.com/eunbining0904/")
                    .webpageUrl("https://nextjs.org/")
                    .startTime(LocalDateTime.of(2022, 6, 22, 19, 30))
                    .endTime(LocalDateTime.of(2022, 6, 22, 21, 30))
                    .salesOption(SalesOption.FLAT_PRICE)
                    .location("예술의 전당")
                    .artist(bigNaughty)
                    .build();

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        artistRepository.deleteAll();

        artistRepository.save(bigNaughty);
        eventRepository.saveAll(Arrays.asList(joelEvent, ryanEvent));
        userRepository.save(user);
    }

    @DisplayName("이벤트의 컨트랙트 주소를 받아올 수 있다.")
    @Test
    void findAllContractAddresses() {
        List<String> allContractAddresses = eventRepository.findAllContractAddresses();
        assertThat(allContractAddresses)
                .contains(joelEventContractAddress, ryanEventContractAddress);
    }

    @DisplayName("이벤트의 티켓들을 ON_SALE, PENDING, SOLD_OUT, EXPIRED로 가져오며, 이를 id 순으로 정렬하여 가져올 수 있다.")
    @Test
    void findAllTickets() {
        // given
        Ticket ticket1SoldOut =
                Ticket.builder()
                        .event(joelEvent)
                        .tokenId(1)
                        .tokenUri("https://token1.uri")
                        .price(100000)
                        .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
                        .build();

        Ticket ticket2SoldOut =
                Ticket.builder()
                        .event(joelEvent)
                        .tokenId(2)
                        .tokenUri("https://token1.uri")
                        .price(100000)
                        .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
                        .build();

        Ticket ticket3Pending =
                Ticket.builder()
                        .event(joelEvent)
                        .tokenId(3)
                        .tokenUri("https://token1.uri")
                        .price(100000)
                        .ticketSalesStatus(TicketSalesStatus.PENDING)
                        .build();

        Ticket ticket4Pending =
                Ticket.builder()
                        .event(joelEvent)
                        .tokenId(4)
                        .tokenUri("https://token1.uri")
                        .price(100000)
                        .ticketSalesStatus(TicketSalesStatus.PENDING)
                        .build();

        Ticket ticket5OnSale =
                Ticket.builder()
                        .event(joelEvent)
                        .tokenId(5)
                        .tokenUri("https://token1.uri")
                        .price(100000)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .build();

        Ticket ticket6OnSale =
                Ticket.builder()
                        .event(joelEvent)
                        .tokenId(6)
                        .tokenUri("https://token1.uri")
                        .price(100000)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .build();

        ticketRepository.saveAll(
                Arrays.asList(
                        ticket1SoldOut,
                        ticket2SoldOut,
                        ticket3Pending,
                        ticket4Pending,
                        ticket5OnSale,
                        ticket6OnSale));

        // when
        Long eventId = joelEvent.getId();
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
        Event event = eventRepository.findByContractAddress(joelEventContractAddress).get();

        // then
        assertThat(event).isEqualTo(joelEvent);
    }
}
