package com.backend.connectable.event.ui;

import com.backend.connectable.event.domain.EventSalesOption;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.event.service.EventService;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import com.backend.connectable.security.JwtAuthenticationFilter;
import com.backend.connectable.security.SecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = EventController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfiguration.class, JwtAuthenticationFilter.class})
    })
@MockBean(JpaMetamodelMappingContext.class)
class EventControllerTest {

    private static final Long EVENT_ID_1 = 1L;
    private static final Long EVENT_ID_2 = 2L;
    private static final Long ARTIST_ID_1 = 1L;
    private static final Long TICKET_ID_1 = 1L;
    private static final Long TICKET_ID_2 = 2L;


    private static final EventResponse EVENT_RESPONSE_1 = new EventResponse(
        EVENT_ID_1,
        "test1",
        "/connectable-events/image_0xtest.jpeg",
        LocalDateTime.now(),
        "description1",
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    private static final EventResponse EVENT_RESPONSE_2 = new EventResponse(
        EVENT_ID_2,
        "test2",
        "/connectable-events/image_0xtest.jpeg",
        LocalDateTime.now(),
        "description2",
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    private static final EventDetailResponse EVENT_DETAIL_RESPONSE = new EventDetailResponse(
        EVENT_ID_1,
        "이씨 콘서트",
        "https://connectable-events.s3.ap-northeast-2.amazonaws.com/image_0xtest.jpeg",
        "빅나티",
        "https://user-images.githubusercontent.com/54073761/179218800-dda72067-3b25-4ca3-b53b-4895c5e49213.jpeg",
        "이씨 콘서트 at Connectable",
        "0x1234abcd",
        LocalDateTime.of(2022, 7, 12, 0, 0),
        LocalDateTime.of(2022, 7, 30, 0, 0),
        "https://twitter.com/iamprogrammerio/status/666930927675797504",
        "https://www.instagram.com/hm_son7/",
        "https://dev-connectable.vercel.app/",
        10,
        9,
        LocalDateTime.of(2022, 8, 1, 18, 0),
        LocalDateTime.of(2022, 8, 1, 19, 0),
        100000,
        "서울특별시 강남구 테헤란로 311 아남타워빌딩 7층",
        EventSalesOption.FLAT_PRICE
    );

    private static final TicketMetadata SAMPLE_TICKET_METADATA = TicketMetadata.builder()
        .name("메타데이터")
        .description("메타데이터 at Connectable")
        .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test2.png")
        .attributes(new HashMap<>(){{
            put("Background", "Yellow");
            put("Artist", "Joel");
            put("Seat", "A5");
        }})
        .build();


    private static final TicketResponse TICKET_RESPONSE_1 = new TicketResponse(
        TICKET_ID_1,
        10000,
        "빅나티",
        LocalDateTime.of(2022, 8, 1, 18, 0),
        "이씨 콘서트 at Connectable",
        TicketSalesStatus.ON_SALE,
        0,
        "https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json",
        false,
        SAMPLE_TICKET_METADATA,
        "0x123456",
        "0xabcd"
    );

    private static final TicketResponse TICKET_RESPONSE_2 = new TicketResponse(
        TICKET_ID_2,
        10000,
        "빅나티",
        LocalDateTime.of(2022, 8, 1, 18, 0),
        "이씨 콘서트 at Connectable",
        TicketSalesStatus.ON_SALE,
        1,
        "https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/2.json",
        false,
        SAMPLE_TICKET_METADATA,
        "0x123456",
        "0xabcd"
    );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @DisplayName("이벤트를 여러개 조회한다.")
    @WithMockUser
    @Test
    void getEventsList() throws Exception {
        // given
        List<EventResponse> mockedEventResponseList = List.of(EVENT_RESPONSE_1, EVENT_RESPONSE_2);

        given(eventService.getList()).willReturn(mockedEventResponseList);

        // expected
        mockMvc.perform(get("/events")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists())
            .andExpect(jsonPath("$[0].name").value("test1"))
            .andExpect(jsonPath("$[0].description").value("description1"))
            .andExpect(jsonPath("$[1].name").value("test2"))
            .andExpect(jsonPath("$[1].description").value("description2"))
            .andDo(print());
    }

    @DisplayName("이벤트 번호를 사용하여 특정 이벤트를 조회한다.")
    @WithMockUser
    @Test
    void getEventDetail() throws Exception {
        // given
        given(eventService.getEventDetail(EVENT_ID_1)).willReturn(EVENT_DETAIL_RESPONSE);

        // expected
        mockMvc.perform(get("/events/{event-id}", EVENT_ID_1)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.artistName").value("빅나티"))
            .andExpect(jsonPath("$.contractAddress").value("0x1234abcd"))
            .andDo(print());
    }

    @DisplayName("이벤트 번호를 사용하여 특정 이벤트에 귀속된 티켓 목록을 조회할 수 있다.")
    @WithMockUser
    @Test
    void getTicketList() throws Exception {
        // given
        given(eventService.getTicketList(EVENT_ID_1)).willReturn(List.of(TICKET_RESPONSE_1, TICKET_RESPONSE_2));

        // expected
        mockMvc.perform(get("/events/{event-id}/tickets", EVENT_ID_1)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].tokenUri")
                .value("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json"))
            .andExpect(jsonPath("$[0].artistName").value("빅나티"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].tokenUri")
                .value("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/2.json"))
            .andExpect(jsonPath("$[1].artistName").value("빅나티"))
            .andDo(print());
    }

    @DisplayName("이벤트 번호와 티켓 번호를 이용하여 티켓의 상세정보를 조회할 수 있다.")
    @WithMockUser
    @Test
    void getTicketInfo() throws Exception {
        // given
        given(eventService.getTicketInfo(EVENT_ID_1, TICKET_ID_1)).willReturn(TICKET_RESPONSE_1);

        // expected
        mockMvc.perform(get("/events/{event-id}/tickets/{ticket-id}", EVENT_ID_1, TICKET_ID_1)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price").value(10000))
            .andExpect(jsonPath("$.ticketSalesStatus").value("ON_SALE"))
            .andDo(print());
    }
}