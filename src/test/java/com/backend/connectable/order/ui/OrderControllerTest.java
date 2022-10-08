package com.backend.connectable.order.ui;

import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.order.domain.OrderStatus;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
import com.backend.connectable.order.domain.repository.OrderRepository;
import com.backend.connectable.order.service.OrderService;
import com.backend.connectable.order.ui.dto.OrderDetailResponse;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @MockBean OrderService orderService;

    @Autowired UserRepository userRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderDetailRepository orderDetailRepository;

    private static final OrderDetailResponse ORDER_DETAIL_RESPONSE1 =
            new OrderDetailResponse(
                    1L,
                    TicketSalesStatus.PENDING,
                    null,
                    10000,
                    1L,
                    1L,
                    1L,
                    OrderStatus.REQUESTED,
                    LocalDateTime.now(),
                    null);

    private static final OrderDetailResponse ORDER_DETAIL_RESPONSE2 =
            new OrderDetailResponse(
                    2L,
                    TicketSalesStatus.PENDING,
                    null,
                    10000,
                    1L,
                    1L,
                    2L,
                    OrderStatus.REQUESTED,
                    LocalDateTime.now(),
                    null);

    @BeforeEach
    void setUp() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
        User user =
                User.builder()
                        .klaytnAddress("0x1234")
                        .nickname("mrlee7")
                        .phoneNumber("010-2222-3333")
                        .privacyAgreement(true)
                        .isActive(true)
                        .build();
        userRepository.save(user);
    }

    @DisplayName("주문 요청시 주문 등록 성공 후 응답한다.")
    @WithUserDetails("0x1234")
    @Test
    void createOrder() throws Exception {
        // given & when
        OrderRequest orderRequest =
                new OrderRequest("이정필", "010-8516-1399", 1L, Arrays.asList(1L, 2L));
        String json = objectMapper.writeValueAsString(orderRequest);

        // expected
        mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저 정보로 요청시 주문 상세 목록을 반환한다.")
    @WithUserDetails("0x1234")
    @Test
    void getOrderDetailList() throws Exception {
        // given & when
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails("0x1234");
        given(orderService.getOrderDetailList(connectableUserDetails))
                .willReturn(Arrays.asList(ORDER_DETAIL_RESPONSE1, ORDER_DETAIL_RESPONSE2));

        // expected
        mockMvc.perform(get("/orders/list").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("주문자명 누락시 ConnectableException 발생하여 주문에 실패한다.")
    @WithUserDetails("0x1234")
    @Test
    void createOrderFailDueToEmptyOrderer() throws Exception {
        // given & when
        OrderRequest orderRequest =
                new OrderRequest("", "010-8516-1399", 1L, Arrays.asList(1L, 2L));
        String json = objectMapper.writeValueAsString(orderRequest);

        // expected
        mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @DisplayName("입금자 연락처 누락시 ConnectableException 발생하여 주문에 실패한다.")
    @WithUserDetails("0x1234")
    @Test
    void createOrderFailDueToEmptyPhoneNumber() throws Exception {
        // given & when
        OrderRequest orderRequest =
                new OrderRequest("", "010-2222-3333", 1L, Arrays.asList(1L, 2L));
        String json = objectMapper.writeValueAsString(orderRequest);

        // expected
        mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @DisplayName("입금자 연락처 규격외 형식 ConnectableException 발생하여 주문에 실패한다.")
    @WithUserDetails("0x1234")
    @Test
    void createOrderFailDueToOutOfStylePhoneNumber() throws Exception {
        // given & when
        OrderRequest orderRequest = new OrderRequest("", "01022223333", 1L, Arrays.asList(1L, 2L));
        String json = objectMapper.writeValueAsString(orderRequest);

        // expected
        mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
