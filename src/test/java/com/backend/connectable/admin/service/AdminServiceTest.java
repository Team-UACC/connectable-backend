package com.backend.connectable.admin.service;

import static com.backend.connectable.fixture.ArtistFixture.createArtistBigNaughty;
import static com.backend.connectable.fixture.EventFixture.createEvent;
import static com.backend.connectable.fixture.TicketFixture.createTicket;
import static com.backend.connectable.fixture.UserFixture.createUserJoel;
import static com.backend.connectable.fixture.UserFixture.createUserMrLee;
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

import java.util.Arrays;
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

    User mrLee = createUserMrLee();
    User joel = createUserJoel();

    Artist bigNaughty = createArtistBigNaughty();
    Event bigNaughtyEvent = createEvent(bigNaughty);

    Ticket bigNaughtyEventTicket1 = createTicket(bigNaughtyEvent, 1);
    Ticket bigNaughtyEventTicket2 = createTicket(bigNaughtyEvent, 2);

    Order order;

    OrderDetail orderDetail1;
    OrderDetail orderDetail2;

    @BeforeEach
    void setUp() {
        saveFixtures();

        order = Order.builder()
            .user(joel)
            .ordererName("조영상")
            .ordererPhoneNumber("010-1234-1234")
            .build();

        orderDetail1 = new OrderDetail(OrderStatus.REQUESTED, null, bigNaughtyEventTicket1);
        orderDetail2 = new OrderDetail(OrderStatus.REQUESTED, null, bigNaughtyEventTicket2);

        order.addOrderDetails(List.of(orderDetail1, orderDetail2));
        orderRepository.save(order);

        given(kasService.sendMyToken(any(String.class), any(Integer.class), any(String.class)))
                .willReturn(new TransactionResponse("Submitted", "0x1234abcd"));
    }

    private void saveFixtures() {
        userRepository.saveAll(Arrays.asList(mrLee, joel));
        artistRepository.save(bigNaughty);
        eventRepository.save(bigNaughtyEvent);
        ticketRepository.saveAll(Arrays.asList(bigNaughtyEventTicket1, bigNaughtyEventTicket2));
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
