package com.backend.connectable;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;

    private static final String JOEL_EVENT_IMG_URL = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/image_0xtest.jpeg";
    private static final String JOEL_EVENT_CONTRACT_ADDRESS = "0xa9dac3f1c04b55d7f8d6df115c47ecbc451c4692";

    private static final String RYAN_EVENT_IMG_URL = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan.jpeg";
    private static final String RYAN_EVENT_CONTRACT_ADDRESS = "0xe99540401ef24aba1b7076ea92c94ec38536c6fb";

    private static final String JOEL_KLIP = "0x3AB31D219d45CE40d6862d68d37De6BB73E21a8D";
    private static final String IHH_KLIP = "0xD466B3aafb86446FFC44868284a9FB76A0ae8BCb";
    private static final String ARTIST_IMAGE = "https://user-images.githubusercontent.com/54073761/179218800-dda72067-3b25-4ca3-b53b-4895c5e49213.jpeg";

    @Override
    public void run(ApplicationArguments args) {
        User admin = User.builder()
            .klaytnAddress(accountPoolAddress)
            .nickname("?????????")
            .phoneNumber("000-0000-0000")
            .privacyAgreement(true)
            .isActive(true)
            .build();

        User joel = User.builder()
            .klaytnAddress(JOEL_KLIP)
            .nickname("??????")
            .phoneNumber("010-1234-5678")
            .privacyAgreement(true)
            .isActive(true)
            .build();

        User ihh = User.builder()
            .klaytnAddress(IHH_KLIP)
            .nickname("??????")
            .phoneNumber("010-9898-8989")
            .privacyAgreement(true)
            .isActive(true)
            .build();
        userRepository.saveAll(Arrays.asList(admin, joel, ihh));

        Artist bigNaughty = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("?????????")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage(ARTIST_IMAGE)
            .build();
        artistRepository.save(bigNaughty);

        Event joelEvent = Event.builder()
            .description("????????? ????????? at Connectable")
            .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
            .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
            .contractAddress(JOEL_EVENT_CONTRACT_ADDRESS)
            .eventName("????????? ?????????")
            .eventImage(JOEL_EVENT_IMG_URL)
            .twitterUrl("https://github.com/joelonsw")
            .instagramUrl("https://www.instagram.com/jyoung_with/")
            .webpageUrl("https://papimon.tistory.com/")
            .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
            .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
            .eventSalesOption(EventSalesOption.FLAT_PRICE)
            .location("??????????????? ????????? ???????????? 311 ?????????????????? 7???")
            .artist(bigNaughty)
            .build();

        Event ryanEvent = Event.builder()
            .description("????????? ????????? at Connectable")
            .salesFrom(LocalDateTime.of(2022, 6, 11, 0, 0))
            .salesTo(LocalDateTime.of(2022, 6, 17, 0, 0))
            .contractAddress(RYAN_EVENT_CONTRACT_ADDRESS)
            .eventName("????????? ?????????")
            .eventImage(RYAN_EVENT_IMG_URL)
            .twitterUrl("https://twitter.com/elonmusk")
            .instagramUrl("https://www.instagram.com/eunbining0904/")
            .webpageUrl("https://nextjs.org/")
            .startTime(LocalDateTime.of(2022, 6, 22, 19, 30))
            .endTime(LocalDateTime.of(2022, 6, 22, 21, 30))
            .eventSalesOption(EventSalesOption.FLAT_PRICE)
            .location("????????? ??????")
            .artist(bigNaughty)
            .build();

        eventRepository.saveAll(Arrays.asList(joelEvent, ryanEvent));

        TicketMetadata joelTicket1Metadata = TicketMetadata.builder()
            .name("?????? ????????? #1")
            .description("????????? ????????? at Connectable")
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
            .tokenId(1)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(joelTicket1Metadata)
            .build();

        TicketMetadata joelTicket2Metadata = TicketMetadata.builder()
            .name("?????? ????????? #2")
            .description("????????? ????????? at Connectable")
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
            .tokenId(2)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(joelTicket2Metadata)
            .build();

        TicketMetadata joelTicket3Metadata = TicketMetadata.builder()
            .name("?????? ????????? #3")
            .description("????????? ????????? at Connectable")
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
            .tokenId(3)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(joelTicket3Metadata)
            .build();

        TicketMetadata joelTicket4Metadata = TicketMetadata.builder()
            .name("?????? ????????? #4")
            .description("????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test4.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A3");
            }})
            .build();

        Ticket joelTicket4 = Ticket.builder()
            .user(admin)
            .event(joelEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/4.json")
            .tokenId(4)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(joelTicket4Metadata)
            .build();

        TicketMetadata joelTicket5Metadata = TicketMetadata.builder()
            .name("?????? ????????? #5")
            .description("????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test5.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A2");
            }})
            .build();

        Ticket joelTicket5 = Ticket.builder()
            .user(admin)
            .event(joelEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/5.json")
            .tokenId(5)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(joelTicket5Metadata)
            .build();

        TicketMetadata joelTicket6Metadata = TicketMetadata.builder()
            .name("?????? ????????? #6")
            .description("????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test6.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A1");
            }})
            .build();

        Ticket joelTicket6 = Ticket.builder()
            .user(admin)
            .event(joelEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/6.json")
            .tokenId(6)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(joelTicket6Metadata)
            .build();
        ticketRepository.saveAll(Arrays.asList(joelTicket1, joelTicket2, joelTicket3, joelTicket4, joelTicket5, joelTicket6));

        TicketMetadata ryanTicket1Metadata = TicketMetadata.builder()
            .name("????????? ????????? #1")
            .description("???????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/images/1.png")
            .attributes(new HashMap<>(){{
                put("Artist", "ryan");
                put("Seat", "A2");
            }})
            .build();

        Ticket ryanTicket1 = Ticket.builder()
            .user(ihh)
            .event(ryanEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/json/1.json")
            .tokenId(1)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(ryanTicket1Metadata)
            .build();

        TicketMetadata ryanTicket2Metadata = TicketMetadata.builder()
            .name("????????? ????????? #2")
            .description("???????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/images/2.png")
            .attributes(new HashMap<>(){{
                put("Artist", "ryan");
                put("Seat", "A6");
            }})
            .build();

        Ticket ryanTicket2 = Ticket.builder()
            .user(admin)
            .event(ryanEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/json/2.json")
            .tokenId(2)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(ryanTicket2Metadata)
            .build();

        TicketMetadata ryanTicket3Metadata = TicketMetadata.builder()
            .name("????????? ????????? #3")
            .description("???????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/images/3.png")
            .attributes(new HashMap<>(){{
                put("Artist", "ryan");
                put("Seat", "A4");
            }})
            .build();

        Ticket ryanTicket3 = Ticket.builder()
            .user(admin)
            .event(ryanEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/json/3.json")
            .tokenId(3)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(ryanTicket3Metadata)
            .build();

        TicketMetadata ryanTicket4Metadata = TicketMetadata.builder()
            .name("????????? ????????? #4")
            .description("???????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/images/4.png")
            .attributes(new HashMap<>(){{
                put("Artist", "ryan");
                put("Seat", "A3");
            }})
            .build();

        Ticket ryanTicket4 = Ticket.builder()
            .user(admin)
            .event(ryanEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/json/4.json")
            .tokenId(4)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(ryanTicket4Metadata)
            .build();

        TicketMetadata ryanTicket5Metadata = TicketMetadata.builder()
            .name("????????? ????????? #5")
            .description("???????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/images/5.png")
            .attributes(new HashMap<>(){{
                put("Artist", "ryan");
                put("Seat", "A1");
            }})
            .build();

        Ticket ryanTicket5 = Ticket.builder()
            .user(admin)
            .event(ryanEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/json/5.json")
            .tokenId(5)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(ryanTicket5Metadata)
            .build();

        TicketMetadata ryanTicket6Metadata = TicketMetadata.builder()
            .name("????????? ????????? #6")
            .description("???????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/images/6.png")
            .attributes(new HashMap<>(){{
                put("Artist", "ryan");
                put("Seat", "A5");
            }})
            .build();

        Ticket ryanTicket6 = Ticket.builder()
            .user(admin)
            .event(ryanEvent)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ryan-event/json/6.json")
            .tokenId(6)
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(ryanTicket6Metadata)
            .build();

        ticketRepository.saveAll(Arrays.asList(ryanTicket1, ryanTicket2, ryanTicket3, ryanTicket4, ryanTicket5, ryanTicket6));
    }
}
