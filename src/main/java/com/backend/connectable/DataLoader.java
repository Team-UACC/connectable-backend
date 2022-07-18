package com.backend.connectable;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.SalesOption;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
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

@Profile("local")
@Transactional
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private static final String EVENT_DESCRIPTION_1 = "별이 빛나는 세상을 걸어가고 있는 호랑이의 모습은 우리 삶의 모습으로 비유합니다. " +
        "빛나는 세계를 가슴 속에 품고 보이지 않고 뚜렷하지 않는 세상을 걸어가지만, 세상의 달빛은 내 눈과 가슴속 구슬에도 또렷이 맺혀 있습니다. " +
        "두 개의 달이 함께 하는 이곳은 현실의 공간을 넘어 어딘가로, 저마다 마음속에 품고 있는 길을 우린 언제나 걷고 있습니다. " +
        "달빛의 끝에서 나를 만나고, 저마다 품고 있는 희망을 발견할 수 있기를 희망합니다.";
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;

    private static final String EVENT_IMG_URL_1 = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/image_0xtest.jpeg";
    private static final String EVENT_IMG_URL_2 = "https://assets.otherside.xyz/otherdeeds/871079decce602d36188f532fe6623a15d8c6817ecd3bcd9b0c3a2933bb51c3b.jpg";
    private static final String EVENT_CONTRACT_ADDRESS = "0xda8E2cdEB663Aa3c3DCf729A2937002C27aA1a81";
    private static final String JOEL_KLIP = "0x3AB31D219d45CE40d6862d68d37De6BB73E21a8D";
    private static final String IHH_KLIP = "0xD466B3aafb86446FFC44868284a9FB76A0ae8BCb";
    private static final String ARTIST_IMAGE = "https://user-images.githubusercontent.com/54073761/179218800-dda72067-3b25-4ca3-b53b-4895c5e49213.jpeg";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User joel = User.builder()
                .klaytnAddress(JOEL_KLIP)
                .nickname("조엘")
                .phoneNumber("01012345678")
                .privacyAgreement(true)
                .isActive(true)
                .build();

        User ihh = User.builder()
            .klaytnAddress(IHH_KLIP)
            .nickname("호현")
            .phoneNumber("01098988989")
            .privacyAgreement(true)
            .isActive(true)
            .build();
        userRepository.saveAll(Arrays.asList(joel, ihh));

        Artist artist = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage(ARTIST_IMAGE)
            .build();
        artistRepository.save(artist);

        Event joelEvent = Event.builder()
            .description("조엘의 콘서트 at Connectable")
            .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
            .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
            .contractAddress(EVENT_CONTRACT_ADDRESS)
            .eventName("조엘의 콘서트")
            .eventImage(EVENT_IMG_URL_1)
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

        Event ihhEvent = Event.builder()
            .description(EVENT_DESCRIPTION_1)
            .salesFrom(LocalDateTime.of(2022, 6, 11, 0, 0))
            .salesTo(LocalDateTime.of(2022, 6, 17, 0, 0))
            .contractAddress(EVENT_CONTRACT_ADDRESS)
            .eventName("밤 하늘의 별")
            .eventImage(EVENT_IMG_URL_2)
            .twitterUrl("https://twitter.com/elonmusk")
            .instagramUrl("https://www.instagram.com/eunbining0904/")
            .webpageUrl("https://nextjs.org/")
            .startTime(LocalDateTime.of(2022, 6, 22, 19, 30))
            .endTime(LocalDateTime.of(2022, 6, 22, 21, 30))
            .salesOption(SalesOption.FLAT_PRICE)
            .location("예술의 전당")
            .salesOption(SalesOption.FLAT_PRICE)
            .artist(artist)
            .build();

        eventRepository.saveAll(Arrays.asList(joelEvent, ihhEvent));

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

        TicketMetadata ihhTicket1Metadata = TicketMetadata.builder()
            .name("밤 하늘의 별 #7")
            .description(EVENT_DESCRIPTION_1)
            .image(EVENT_IMG_URL_2)
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A4");
            }})
            .build();

        Ticket ihhTicket1 = Ticket.builder()
            .user(ihh)
            .event(ihhEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/3.json")
            .price(100000)
            .onSale(true)
            .ticketMetadata(ihhTicket1Metadata)
            .build();

        ticketRepository.saveAll(Arrays.asList(joelTicket1, joelTicket2, joelTicket3, ihhTicket1));
    }
}
