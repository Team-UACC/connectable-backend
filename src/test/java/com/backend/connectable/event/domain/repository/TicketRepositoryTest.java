package com.backend.connectable.event.domain.repository;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.EventSalesOption;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EntityManager em;

    User joel = User.builder()
            .klaytnAddress("0x1234")
            .nickname("Joel")
            .phoneNumber("010-1234-5678")
            .privacyAgreement(true)
            .isActive(true)
            .build();

    Artist artist = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage("https://image.url")
            .build();

    Event joelEvent = Event.builder()
            .description("조엘의 콘서트 at Connectable")
            .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
            .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
            .contractAddress("0x123456")
            .eventName("조엘의 콘서트")
            .eventImage("https://image.url")
            .twitterUrl("https://github.com/joelonsw")
            .instagramUrl("https://www.instagram.com/jyoung_with/")
            .webpageUrl("https://papimon.tistory.com/")
            .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
            .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
            .eventSalesOption(EventSalesOption.FLAT_PRICE)
            .artist(artist)
            .build();

    TicketMetadata joelTicket1Metadata = TicketMetadata.builder()
            .name("조엘 콘서트 #1")
            .description("조엘의 콘서트 at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test1.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A6");
            }})
            .build();

    Ticket joelTicket1 = Ticket.builder()
            .user(joel)
            .event(joelEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json")
            .price(100000)
            .onSale(true)
            .ticketMetadata(joelTicket1Metadata)
            .build();

    @BeforeEach
    void setUp() {
        userRepository.save(joel);
        artistRepository.save(artist);
        eventRepository.save(joelEvent);
    }

    @DisplayName("Ticket을 저장할 수 있다.")
    @Test
    void saveTicket() {
        // given & when
        Ticket savedTicket = ticketRepository.save(joelTicket1);

        // then
        assertThat(savedTicket.getUser()).isEqualTo(joel);
        assertThat(savedTicket.getEvent()).isEqualTo(joelEvent);
        assertThat(savedTicket.getPrice()).isEqualTo(100000);
        assertThat(savedTicket.isOnSale()).isEqualTo(true);
        assertThat(savedTicket.getTicketMetadata().getName()).isEqualTo("조엘 콘서트 #1");
        assertThat(savedTicket.getTicketMetadata().getAttributes()).hasSize(3);
    }
}
