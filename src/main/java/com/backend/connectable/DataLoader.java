package com.backend.connectable;

import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

@Profile("dev")
@Transactional
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;

    private static final String EVENT_IMG_URL = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/image_0xtest.jpeg";
    private static final String EVENT_CONTRACT_ADDRESS = "0xda8E2cdEB663Aa3c3DCf729A2937002C27aA1a81";
    private static final String JOEL_KLIP = "0x3AB31D219d45CE40d6862d68d37De6BB73E21a8D";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User joel = User.builder()
                .klaytnAddress(JOEL_KLIP)
                .nickname("조엘")
                .phoneNumber("01012345678")
                .privacyAgreement(true)
                .isActive(true)
                .build();
        userRepository.save(joel);

        Artist artist = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage(EVENT_IMG_URL)
            .build();
        artistRepository.save(artist);

        Event joelEvent = Event.builder()
                .description("조엘의 콘서트 at Connectable")
                .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
                .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
                .contractAddress(EVENT_CONTRACT_ADDRESS)
                .eventName("조엘의 콘서트")
                .eventImage(EVENT_IMG_URL)
                .twitterUrl("https://github.com/joelonsw")
                .instagramUrl("https://www.instagram.com/jyoung_with/")
                .webpageUrl("https://papimon.tistory.com/")
                .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
                .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
                .salesOption(SalesOption.FLAT_PRICE)
                .location("서울특별시 강남구 테헤란로 311 아남타워빌딩 7층")
                .salesOption(SalesOption.FLAT_PRICE)
                .artist(artist)
                .build();
        eventRepository.save(joelEvent);

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

        TicketMetadata joelTicket2Metadata = TicketMetadata.builder()
                .name("조엘 콘서트 #2")
                .description("조엘의 콘서트 at Connectable")
                .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test2.png")
                .attributes(new HashMap<>(){{
                    put("Background", "Yellow");
                    put("Artist", "Joel");
                    put("Seat", "A5");
                }})
                .build();

        Ticket joelTicket2 = Ticket.builder()
                .user(joel)
                .event(joelEvent)
                .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/2.json")
                .price(100000)
                .onSale(false)
                .ticketMetadata(joelTicket2Metadata)
                .build();

        TicketMetadata joelTicket3Metadata = TicketMetadata.builder()
                .name("조엘 콘서트 #3")
                .description("조엘의 콘서트 at Connectable")
                .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test3.png")
                .attributes(new HashMap<>(){{
                    put("Background", "Yellow");
                    put("Artist", "Joel");
                    put("Seat", "A4");
                }})
                .build();

        Ticket joelTicket3 = Ticket.builder()
                .user(joel)
                .event(joelEvent)
                .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/3.json")
                .price(100000)
                .onSale(true)
                .ticketMetadata(joelTicket3Metadata)
                .build();

        ticketRepository.saveAll(Arrays.asList(joelTicket1, joelTicket2, joelTicket3));
    }
}
