package com.backend.connectable.acceptance;

import static com.backend.connectable.fixture.ArtistFixture.createArtistBigNaughty;
import static com.backend.connectable.fixture.EventFixture.createEventValidContractAddressMockedKas;
import static com.backend.connectable.fixture.TicketFixture.createTicket;
import static com.backend.connectable.fixture.UserFixture.createUserJoel;
import static com.backend.connectable.fixture.UserFixture.createUserMrLee;
import static com.backend.connectable.kas.service.mockserver.KasMockRequest.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.backend.connectable.admin.ui.dto.EventIssueRequest;
import com.backend.connectable.admin.ui.dto.TokenIssueRequest;
import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.kas.service.mockserver.KasServiceMockSetup;
import com.backend.connectable.order.domain.Order;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.security.custom.JwtProvider;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminAcceptanceTest extends KasServiceMockSetup {

    @LocalServerPort public int port;

    @Autowired protected OrderDetailRepository orderDetailRepository;

    @Autowired protected UserRepository userRepository;

    @Autowired protected EventRepository eventRepository;

    @Autowired protected TicketRepository ticketRepository;

    @Autowired protected ArtistRepository artistRepository;

    @Autowired protected OrderRepository orderRepository;

    @Autowired private DatabaseCleanUp databaseCleanUp;

    @Value("${jwt.admin-payload}")
    private String adminPayload;

    @Autowired private JwtProvider jwtProvider;

    User mrLee = createUserMrLee();
    User joel = createUserJoel();

    Artist bigNaughty = createArtistBigNaughty();
    Event bigNaughtyEvent = createEventValidContractAddressMockedKas(bigNaughty);

    Ticket bigNaughtyEventTicket1 = createTicket(bigNaughtyEvent, 1);
    Ticket bigNaughtyEventTicket2 = createTicket(bigNaughtyEvent, 2);

    Order joelOrder;

    OrderDetail joelOrderDetail1;
    OrderDetail joelOrderDetail2;

    @BeforeEach
    void setUpData() {
        RestAssured.port = port;
        databaseCleanUp.execute();
        userRepository.saveAll(Arrays.asList(mrLee, joel));
        artistRepository.save(bigNaughty);
        eventRepository.save(bigNaughtyEvent);
        ticketRepository.saveAll(Arrays.asList(bigNaughtyEventTicket1, bigNaughtyEventTicket2));
        makeJoelOrder();
    }

    private void makeJoelOrder() {
        joelOrder =
                Order.builder()
                        .user(joel)
                        .ordererName("조영상")
                        .ordererPhoneNumber("010-0000-0000")
                        .build();
        joelOrderDetail1 = new OrderDetail(OrderStatus.REQUESTED, null, bigNaughtyEventTicket1);
        joelOrderDetail2 = new OrderDetail(OrderStatus.REQUESTED, null, bigNaughtyEventTicket2);
        joelOrder.addOrderDetails(List.of(joelOrderDetail1, joelOrderDetail2));
        orderRepository.save(joelOrder);
    }

    @DisplayName("어드민 토큰을 활용하여 order-detail을 paid -> transfer_success로 변경할 수 있다.")
    @Test
    void orderDetailToPaid() {
        // when
        ExtractableResponse<Response> response = 어드민_오더_상세_PAID_변경(joelOrderDetail1.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderDetail modifiedOrderDetail =
                orderDetailRepository.findById(joelOrderDetail1.getId()).get();
        assertThat(modifiedOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.TRANSFER_SUCCESS);
    }

    @DisplayName("어드민 토큰을 활용하여 order-detail을 unpaid로 변경할 수 있다.")
    @Test
    void orderDetailToUnpaid() {
        // when
        ExtractableResponse<Response> response = 어드민_오더_상세_UNPAID_변경(joelOrderDetail1.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderDetail modifiedOrderDetail =
                orderDetailRepository.findById(joelOrderDetail1.getId()).get();
        assertThat(modifiedOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.UNPAID);
    }

    @DisplayName("어드민 토큰을 활용하여 order-detail을 refund로 변경할 수 있다.")
    @Test
    void orderDetailToRefund() {
        // when
        ExtractableResponse<Response> response = 어드민_오더_상세_REFUND_변경(joelOrderDetail1.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderDetail modifiedOrderDetail =
                orderDetailRepository.findById(joelOrderDetail1.getId()).get();
        assertThat(modifiedOrderDetail.getOrderStatus()).isEqualTo(OrderStatus.REFUND);
    }

    @DisplayName("어드민 토큰을 활용해 이벤트를 발행할 수 있다.")
    @Test
    void deployEvent() {
        // given
        long eventCountBefore = eventRepository.count();
        EventIssueRequest eventIssueRequest =
                new EventIssueRequest(
                        "Contract",
                        "CON",
                        VALID_ALIAS,
                        "인수 이벤트",
                        "인수 이벤트 설명",
                        "https://image.url",
                        "https://twitter.url",
                        "https://instagram.url",
                        "https://webpage.url",
                        "인수 이벤트 위치",
                        bigNaughty.getId(),
                        LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2001, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2002, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2003, 1, 1, 0, 0, 0));

        // when
        ExtractableResponse<Response> response = 어드민_이벤트_발행(eventIssueRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long eventCountAfter = eventRepository.count();
        assertThat(eventCountAfter - eventCountBefore).isEqualTo(1L);
    }

    @DisplayName("어드민 토큰을 통해 이벤트의 토큰을 민팅할 수 있다.")
    @Test
    void minTokens() {
        // given
        String validTokenUri =
                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/1.json";
        TokenIssueRequest tokenIssueRequest =
                new TokenIssueRequest(
                        VALID_CONTRACT_ADDRESS,
                        VALID_TOKEN_ID_BY_INT,
                        VALID_TOKEN_ID_BY_INT,
                        validTokenUri,
                        10000);

        // when
        ExtractableResponse<Response> response = 어드민_토큰_발행(tokenIssueRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 어드민_오더_상세_PAID_변경(Long orderDetailId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .auth()
                .oauth2(generateAdminToken())
                .patch("/admin/order-details/{order-detail-id}/paid", orderDetailId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_오더_상세_UNPAID_변경(Long orderDetailId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .auth()
                .oauth2(generateAdminToken())
                .patch("/admin/order-details/{order-detail-id}/unpaid", orderDetailId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_오더_상세_REFUND_변경(Long orderDetailId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .auth()
                .oauth2(generateAdminToken())
                .patch("/admin/order-details/{order-detail-id}/refund", orderDetailId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_이벤트_발행(EventIssueRequest eventIssueRequest) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .auth()
                .oauth2(generateAdminToken())
                .body(eventIssueRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/admin/deploy-event")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> 어드민_토큰_발행(TokenIssueRequest tokenIssueRequest) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .auth()
                .oauth2(generateAdminToken())
                .body(tokenIssueRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/admin/mint-tokens")
                .then()
                .log()
                .all()
                .extract();
    }

    private String generateAdminToken() {
        return jwtProvider.generateToken(adminPayload);
    }
}
