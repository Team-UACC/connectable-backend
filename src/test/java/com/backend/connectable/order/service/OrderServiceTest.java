package com.backend.connectable.order.service;

import static com.backend.connectable.fixture.ArtistFixture.createArtistBigNaughty;
import static com.backend.connectable.fixture.EventFixture.createEvent;
import static com.backend.connectable.fixture.TicketFixture.createTicketWithSalesStatus;
import static com.backend.connectable.fixture.UserFixture.createUserMrLee;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.order.ui.dto.OrderDetailResponse;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.order.ui.dto.OrderResponse;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderServiceTest {

    @Autowired UserRepository userRepository;

    @Autowired ArtistRepository artistRepository;

    @Autowired EventRepository eventRepository;

    @Autowired TicketRepository ticketRepository;

    @Autowired OrderDetailRepository orderDetailRepository;

    @Autowired OrderRepository orderRepository;

    @Autowired OrderService orderService;

    private final User user = createUserMrLee();
    private final Artist artist = createArtistBigNaughty();
    private final Event event = createEvent(artist);
    private final Ticket ticket1 = createTicketWithSalesStatus(event, 1, TicketSalesStatus.ON_SALE);
    private final Ticket ticket2 = createTicketWithSalesStatus(event, 2, TicketSalesStatus.ON_SALE);
    private final Ticket ticket3 = createTicketWithSalesStatus(event, 3, TicketSalesStatus.ON_SALE);

    @BeforeEach
    void setUp() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(user);
        artistRepository.save(artist);
        eventRepository.save(event);
        ticketRepository.saveAll(List.of(ticket1, ticket2, ticket3));
    }

    @DisplayName("티켓을 구매하였을때, 주문정보 및 주문상세정보가 등록된다.")
    @Test
    void createOrder() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user.getKlaytnAddress());
        OrderRequest orderRequest =
                new OrderRequest(
                        user.getNickname(),
                        user.getPhoneNumber(),
                        event.getId(),
                        Arrays.asList(ticket1.getId(), ticket2.getId()));

        // when
        OrderResponse orderResponse =
                orderService.createOrder(connectableUserDetails, orderRequest);

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
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user.getKlaytnAddress());
        OrderRequest orderRequest1 =
                new OrderRequest(
                        user.getNickname(),
                        user.getPhoneNumber(),
                        event.getId(),
                        List.of(ticket1.getId(), ticket2.getId()));
        OrderRequest orderRequest2 =
                new OrderRequest(
                        user.getNickname(),
                        user.getPhoneNumber(),
                        event.getId(),
                        List.of(ticket3.getId()));
        orderService.createOrder(connectableUserDetails, orderRequest1);
        orderService.createOrder(connectableUserDetails, orderRequest2);

        // when
        List<OrderDetailResponse> orderDetailResponses =
                orderService.getOrderDetailList(connectableUserDetails);

        // then
        assertEquals(3L, orderDetailResponses.size());

        assertThat(orderDetailResponses.get(0).getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.PENDING);
        assertThat(orderDetailResponses.get(0).getOrderStatus()).isEqualTo(OrderStatus.REQUESTED);
        assertThat(orderDetailResponses.get(0).getModifiedDate()).isNotNull();
        assertThat(orderDetailResponses.get(0).getTicketMetadata().getName())
                .isEqualTo(ticket1.getTicketMetadata().getName());
        assertThat(orderDetailResponses.get(0).getEventId()).isEqualTo(event.getId());
        assertThat(orderDetailResponses.get(0).getPrice()).isEqualTo(ticket1.getPrice());

        assertThat(orderDetailResponses.get(1).getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.PENDING);
        assertThat(orderDetailResponses.get(1).getOrderStatus()).isEqualTo(OrderStatus.REQUESTED);
        assertThat(orderDetailResponses.get(1).getModifiedDate()).isNotNull();
        assertThat(orderDetailResponses.get(1).getTicketMetadata().getName())
                .isEqualTo(ticket2.getTicketMetadata().getName());
        assertThat(orderDetailResponses.get(1).getEventId()).isEqualTo(event.getId());
        assertThat(orderDetailResponses.get(1).getPrice()).isEqualTo(ticket2.getPrice());

        assertThat(orderDetailResponses.get(0).getModifiedDate())
                .isGreaterThanOrEqualTo(orderDetailResponses.get(1).getModifiedDate());
    }

    @DisplayName("티켓 id가 0이고, 이거만 List에 있다면, event에 대응되는 티켓중 판매가능한 티켓을 orderDetail로 주문한다.")
    @Test
    void ticketIdZero() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user.getKlaytnAddress());
        OrderRequest orderRequest =
                new OrderRequest("조영상", "010-9999-5555", event.getId(), List.of(0L));

        // when
        OrderResponse orderResponse =
                orderService.createOrder(connectableUserDetails, orderRequest);

        // then
        assertThat(orderResponse.getStatus()).isEqualTo("success");
        Ticket updatedTicket1 = ticketRepository.findById(ticket1.getId()).get();
        assertThat(updatedTicket1.getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
    }

    @DisplayName("List에 담긴 0의 갯수만큼 event에 대응되는 티켓중 판매가능한 티켓을 orderDetail로 주문한다.")
    @Test
    void ticketIdMultipleZero() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user.getKlaytnAddress());
        OrderRequest orderRequest =
                new OrderRequest(
                        user.getNickname(),
                        user.getPhoneNumber(),
                        event.getId(),
                        List.of(0L, 0L, 0L));

        // when
        OrderResponse orderResponse =
                orderService.createOrder(connectableUserDetails, orderRequest);

        // then
        assertThat(orderResponse.getStatus()).isEqualTo("success");
        Ticket updatedTicket1 = ticketRepository.findById(ticket1.getId()).get();
        Ticket updatedTicket2 = ticketRepository.findById(ticket2.getId()).get();
        Ticket updatedTicket3 = ticketRepository.findById(ticket3.getId()).get();
        assertThat(updatedTicket1.getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
        assertThat(updatedTicket2.getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
        assertThat(updatedTicket3.getTicketSalesStatus()).isEqualTo(TicketSalesStatus.PENDING);
    }

    @DisplayName("List에 담긴 0의 갯수가 event에 대응되는 티켓중 판매가능한 티켓의 수보다 크다면 예외가 발생한다.")
    @Test
    void ticketIdMultipleZeroOverLimit() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user.getKlaytnAddress());
        OrderRequest orderRequest =
                new OrderRequest(
                        user.getNickname(),
                        user.getPhoneNumber(),
                        event.getId(),
                        List.of(0L, 0L, 0L, 0L, 0L, 0L));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(connectableUserDetails, orderRequest))
                .isInstanceOf(ConnectableException.class);
    }
}
