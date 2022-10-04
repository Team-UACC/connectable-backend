package com.backend.connectable.admin.service;

import static com.backend.connectable.admin.service.AdminIssueDtoFixture.eventIssueRequest;
import static com.backend.connectable.fixture.ArtistFixture.createArtistBigNaughty;
import static com.backend.connectable.fixture.EventFixture.createEvent;
import static com.backend.connectable.fixture.TicketFixture.createTicket;
import static com.backend.connectable.fixture.UserFixture.createUserJoel;
import static com.backend.connectable.fixture.UserFixture.createUserMrLee;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.backend.connectable.admin.ui.dto.EventIssueRequest;
import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.contract.dto.ContractDeployResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
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

    Order joelOrder;

    OrderDetail joelOrderDetail1;
    OrderDetail joelOrderDetail2;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(Arrays.asList(mrLee, joel));
        artistRepository.save(bigNaughty);
        eventRepository.save(bigNaughtyEvent);
        ticketRepository.saveAll(Arrays.asList(bigNaughtyEventTicket1, bigNaughtyEventTicket2));
    }

    private void makeJoelOrder() {
        joelOrder = Order.builder()
            .user(joel)
            .ordererName("조영상")
            .ordererPhoneNumber("010-1234-1234")
            .build();
        joelOrderDetail1 = new OrderDetail(OrderStatus.REQUESTED, null, bigNaughtyEventTicket1);
        joelOrderDetail2 = new OrderDetail(OrderStatus.REQUESTED, null, bigNaughtyEventTicket2);
        joelOrder.addOrderDetails(List.of(joelOrderDetail1, joelOrderDetail2));
        orderRepository.save(joelOrder);
    }

    @DisplayName("OrderDetail의 ID에 대해 Paid로 상태를 변경, 티켓을 전송할 수 있으며 Transfer Success로 변환이 가능하다.")
    @Test
    void orderDetailToPaid() {
        // given
        makeJoelOrder();
        String txStatus = "Submitted";
        String txHash = "0x1234abcd";
        given(kasService.sendMyToken(any(String.class), any(Integer.class), any(String.class)))
            .willReturn(new TransactionResponse(txStatus, txHash));
        Long orderDetailId = joelOrderDetail1.getId();

        // when
        adminService.orderDetailToPaid(orderDetailId);

        // then
        OrderDetail resultOrderDetail = orderDetailRepository.findById(orderDetailId).get();
        assertThat(resultOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.TRANSFER_SUCCESS);
        assertThat(resultOrderDetail.getTxHash()).isEqualTo(txHash);
        assertThat(resultOrderDetail.getTicket().getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.SOLD_OUT);
    }

    @DisplayName("OrderDetail의 ID에 대해 Unpaid로 상태를 변경할 수 있다.")
    @Test
    void orderDetailToUnpaid() {
        // given
        makeJoelOrder();
        Long orderDetailId = joelOrderDetail2.getId();

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
        makeJoelOrder();
        Long orderDetailId = joelOrderDetail1.getId();

        // when
        adminService.orderDetailToRefund(orderDetailId);

        // then
        OrderDetail resultOrderDetail = orderDetailRepository.findById(orderDetailId).get();
        assertThat(resultOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.REFUND);
        assertThat(resultOrderDetail.getTicket().getTicketSalesStatus())
                .isEqualTo(TicketSalesStatus.ON_SALE);
    }

    @DisplayName("이벤트를 발행할 수 있다.")
    @Test
    void issueEvent() throws InterruptedException {
        // given
        EventIssueRequest eventIssueRequest = eventIssueRequest(bigNaughty);
        String name = eventIssueRequest.getContractName();
        String symbol = eventIssueRequest.getContractSymbol();
        String alias = eventIssueRequest.getContractAlias();
        String deployedAddress = "0x9876abcd";

        given(kasService.deployMyContract(name, symbol, alias))
            .willReturn(new ContractDeployResponse("0x1234", "success", "0xabcd"));
        given(kasService.getMyContractByAlias(eventIssueRequest.getContractAlias()))
            .willReturn(new ContractItemResponse(deployedAddress, alias, "8217", name, symbol));

        // when
        adminService.issueEvent(eventIssueRequest);

        // then
        Event savedEvent = eventRepository.findByContractAddress(deployedAddress).get();
        assertThat(savedEvent.getArtistName()).isEqualTo(bigNaughty.getArtistName());
        assertThat(savedEvent.getEventName()).isEqualTo(eventIssueRequest.getEventName());
        assertThat(savedEvent.getEventImage()).isEqualTo(eventIssueRequest.getEventImage());
        assertThat(savedEvent.getDescription()).isEqualTo(eventIssueRequest.getEventDescription());
        assertThat(savedEvent.getContractName()).isEqualTo(eventIssueRequest.getContractName());
        assertThat(savedEvent.getSalesTo()).isEqualTo(eventIssueRequest.getEventSalesTo());
        assertThat(savedEvent.getEndTime()).isEqualTo(eventIssueRequest.getEventEndTime());
    }

    @DisplayName("존재하지 않는 아티스트의 ID로 이벤트를 발행할 수 없다.")
    @Test
    void issueEventWithValidArtistId() {
        // given
        EventIssueRequest eventIssueRequest = eventIssueRequest(bigNaughty);
        eventIssueRequest.setEventArtistId(1000L);
        String name = eventIssueRequest.getContractName();
        String symbol = eventIssueRequest.getContractSymbol();
        String alias = eventIssueRequest.getContractAlias();
        String deployedAddress = "0x9876abcd";

        given(kasService.deployMyContract(name, symbol, alias))
            .willReturn(new ContractDeployResponse("0x1234", "success", "0xabcd"));
        given(kasService.getMyContractByAlias(eventIssueRequest.getContractAlias()))
            .willReturn(new ContractItemResponse(deployedAddress, alias, "8217", name, symbol));

        // when & then
        assertThatCode(() -> adminService.issueEvent(eventIssueRequest))
            .isInstanceOf(ConnectableException.class);
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
