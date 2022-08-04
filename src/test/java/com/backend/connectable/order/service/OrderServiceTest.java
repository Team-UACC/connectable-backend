package com.backend.connectable.order.service;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.order.ui.dto.OrderDetailResponse;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.order.ui.dto.OrderResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    private User user;
    private Artist artist;
    private Event event;
    private TicketMetadata ticket1Metadata;
    private Ticket ticket1;
    private TicketMetadata ticket2Metadata;
    private Ticket ticket2;
    private TicketMetadata ticket3Metadata;
    private Ticket ticket3;

    private final String userKlaytnAddress = "0x12345678";
    private final String userNickname = "leejp";
    private final String userPhoneNumber = "010-3333-7777";
    private final boolean userPrivacyAgreement = true;
    private final boolean userIsActive = true;

    @BeforeEach
    void setUp() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder()
            .klaytnAddress(userKlaytnAddress)
            .nickname(userNickname)
            .phoneNumber(userPhoneNumber)
            .privacyAgreement(userPrivacyAgreement)
            .isActive(userIsActive)
            .build();

        artist = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage("https://image.url")
            .build();

        event = Event.builder()
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

        ticket1Metadata = TicketMetadata.builder()
            .name("조엘 콘서트 #1")
            .description("조엘의 콘서트 at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test1.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A6");
            }})
            .build();

        ticket1 = Ticket.builder()
            .user(user)
            .event(event)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json")
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(ticket1Metadata)
            .build();

        ticket2Metadata = TicketMetadata.builder()
            .name("조엘 콘서트 #2")
            .description("조엘의 콘서트 at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test2.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A5");
            }})
            .build();

        ticket2 = Ticket.builder()
            .user(user)
            .event(event)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/2.json")
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(ticket2Metadata)
            .build();

        ticket3Metadata = TicketMetadata.builder()
            .name("조엘 콘서트 #3")
            .description("조엘의 콘서트 at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test3.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A5");
            }})
            .build();

        ticket3 = Ticket.builder()
            .user(user)
            .event(event)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/3.json")
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(ticket3Metadata)
            .build();

        userRepository.save(user);
        artistRepository.save(artist);
        eventRepository.save(event);
        ticketRepository.saveAll(Arrays.asList(ticket1, ticket2, ticket3));
    }

    @DisplayName("티켓을 구매하였을때, 주문정보 및 주문상세정보가 등록된다.")
    @Test
    void createOrder() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user);
        OrderRequest orderRequest = new OrderRequest("이정필", "010-3333-7777",
            Arrays.asList(ticket1.getId(), ticket2.getId()), 30000);

        // when
        OrderResponse orderResponse = orderService.createOrder(connectableUserDetails, orderRequest);

        // then
        assertThat(orderResponse.getStatus()).isEqualTo("success");
        Ticket updatedTicket1 = ticketRepository.findById(ticket1.getId()).get();
        Ticket updatedTicket2 = ticketRepository.findById(ticket2.getId()).get();
        assertThat(updatedTicket1.getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
        assertThat(updatedTicket2.getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
    }

    @DisplayName("유저 정보를 이용하여 구매내역을 확인할 수 있다.")
    @Test
    void getOrderDetailList() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user);
        OrderRequest orderRequest1 = new OrderRequest("이정필", "010-3333-7777",
            Arrays.asList(ticket1.getId(), ticket2.getId()), 200000);
        OrderRequest orderRequest2 = new OrderRequest("이정필", "010-3333-7777",
            Arrays.asList(ticket3.getId()), 100000);

        // when
        orderService.createOrder(connectableUserDetails, orderRequest1);
        orderService.createOrder(connectableUserDetails, orderRequest2);
        List<OrderDetailResponse> orderDetailResponse = orderService.getOrderDetailList(connectableUserDetails);

        assertThat(orderDetailResponse.get(0).getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
        assertThat(orderDetailResponse.get(0).getOrderStatus()).isEqualTo(OrderStatus.REQUESTED);
        assertThat(orderDetailResponse.get(0).getModifiedDate()).isNotNull();
        assertThat(orderDetailResponse.get(1).getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
        assertThat(orderDetailResponse.get(1).getOrderStatus()).isEqualTo(OrderStatus.REQUESTED);
        assertThat(orderDetailResponse.get(1).getModifiedDate()).isNotNull();
    }
}
