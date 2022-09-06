package com.backend.connectable.admin.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.order.domain.Order;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AdminServiceTest {

    @Autowired private AdminService adminService;

    @Autowired private OrderDetailRepository orderDetailRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private EventRepository eventRepository;

    @Autowired private TicketRepository ticketRepository;

    @Autowired private ArtistRepository artistRepository;

    @Autowired private OrderRepository orderRepository;

    @MockBean private KasService kasService;

    User admin =
            User.builder()
                    .klaytnAddress("0x1111")
                    .nickname("어드민")
                    .phoneNumber("000-0000-0000")
                    .privacyAgreement(true)
                    .isActive(true)
                    .build();

    User joel =
            User.builder()
                    .klaytnAddress("0x1234")
                    .nickname("조엘")
                    .phoneNumber("010-1234-5678")
                    .privacyAgreement(true)
                    .isActive(true)
                    .build();

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

    Event joelEvent =
            Event.builder()
                    .description("조엘의 콘서트 at Connectable")
                    .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
                    .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
                    .contractAddress("JOEL_EVENT_CONTRACT_ADDRESS")
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

    TicketMetadata joelTicket4Metadata =
            TicketMetadata.builder()
                    .name("조엘 콘서트 #4")
                    .description("조엘의 콘서트 at Connectable")
                    .image(
                            "https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test4.png")
                    .attributes(
                            new HashMap<>() {
                                {
                                    put("Background", "Yellow");
                                    put("Artist", "Joel");
                                    put("Seat", "A3");
                                }
                            })
                    .build();

    Ticket joelTicket4 =
            Ticket.builder()
                    .event(joelEvent)
                    .tokenUri(
                            "https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/4.json")
                    .tokenId(4)
                    .price(100000)
                    .ticketSalesStatus(TicketSalesStatus.PENDING)
                    .ticketMetadata(joelTicket4Metadata)
                    .build();

    TicketMetadata joelTicket5Metadata =
            TicketMetadata.builder()
                    .name("조엘 콘서트 #5")
                    .description("조엘의 콘서트 at Connectable")
                    .image(
                            "https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test5.png")
                    .attributes(
                            new HashMap<>() {
                                {
                                    put("Background", "Yellow");
                                    put("Artist", "Joel");
                                    put("Seat", "A2");
                                }
                            })
                    .build();

    Ticket joelTicket5 =
            Ticket.builder()
                    .event(joelEvent)
                    .tokenUri(
                            "https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/5.json")
                    .tokenId(5)
                    .price(100000)
                    .ticketSalesStatus(TicketSalesStatus.PENDING)
                    .ticketMetadata(joelTicket5Metadata)
                    .build();

    Order order =
            Order.builder()
                    .user(joel)
                    .ordererName("조영상")
                    .ordererPhoneNumber("010-1234-5678")
                    .build();

    OrderDetail orderDetail1;
    OrderDetail orderDetail2;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(Arrays.asList(admin, joel));
        artistRepository.save(bigNaughty);
        eventRepository.save(joelEvent);
        ticketRepository.saveAll(Arrays.asList(joelTicket4, joelTicket5));

        orderDetail1 = new OrderDetail(OrderStatus.REQUESTED, null, joelTicket4);
        orderDetail2 = new OrderDetail(OrderStatus.REQUESTED, null, joelTicket5);
        List<OrderDetail> orderDetails = Arrays.asList(orderDetail1, orderDetail2);
        order.addOrderDetails(orderDetails);

        orderRepository.save(order);

        given(kasService.sendMyToken(any(String.class), any(Integer.class), any(String.class)))
                .willReturn(new TransactionResponse("Submitted", "0x1234abcd"));
    }

    @DisplayName("OrderDetail의 ID에 대해 Paid로 상태를 변경, 티켓을 전송할 수 있으며 Transfer Success로 변환이 가능하다.")
    @Test
    void orderDetailToPaid() {
        // given
        Long orderDetailId = orderDetail1.getId();

        // when
        adminService.orderDetailToPaid(orderDetailId);

        // then
        OrderDetail resultOrderDetail = orderDetailRepository.findById(orderDetailId).get();
        assertThat(resultOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.TRANSFER_SUCCESS);
        assertThat(resultOrderDetail.getTxHash()).isEqualTo("0x1234abcd");
        assertThat(resultOrderDetail.getTicket().getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.SOLD_OUT);
    }

    @DisplayName("OrderDetail의 ID에 대해 Unpaid로 상태를 변경할 수 있다.")
    @Test
    void orderDetailToUnpaid() {
        // given
        Long orderDetailId = orderDetail2.getId();

        // when
        adminService.orderDetailToUnpaid(orderDetailId);

        // then
        OrderDetail resultOrderDetail = orderDetailRepository.findById(orderDetailId).get();
        assertThat(resultOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.UNPAID);
        assertThat(resultOrderDetail.getTicket().getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.ON_SALE);
    }

    @DisplayName("OrderDetail의 ID에 대해 Refund로 상태를 변경할 수 있다.")
    @Test
    void orderDetailToRefund() {
        // given
        Long orderDetailId = orderDetail1.getId();

        // when
        adminService.orderDetailToRefund(orderDetailId);

        // then
        OrderDetail resultOrderDetail = orderDetailRepository.findById(orderDetailId).get();
        assertThat(resultOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.REFUND);
        assertThat(resultOrderDetail.getTicket().getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.ON_SALE);
    }

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
    }
}
