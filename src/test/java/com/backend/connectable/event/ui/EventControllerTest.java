package com.backend.connectable.event.ui;

import com.backend.connectable.event.domain.SalesOption;
import com.backend.connectable.event.service.EventService;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.security.JwtAuthenticationFilter;
import com.backend.connectable.security.SecurityConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
class EventControllerTest {

    private static final Long EVENT_ID = 1L;
    private static final Long ARTIST_ID = 1L;

    private static final EventDetailResponse EVENT_DETAIL_RESPONSE = new EventDetailResponse(
        EVENT_ID,
        "이씨 콘서트",
        "https://connectable-events.s3.ap-northeast-2.amazonaws.com/image_0xtest.jpeg",
        "빅나티",
        LocalDateTime.of(2022, 7, 12, 0, 0),
        "이씨 콘서트 at Connectable",
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
        SalesOption.FLAT_PRICE
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
        List<EventResponse> mockedEventResponseList = List.of(
            new EventResponse(
                1L,
                "test1",
                "/connectable-events/image_0xtest.jpeg",
                LocalDateTime.now(),
                "description1",
                LocalDateTime.now(),
                LocalDateTime.now()
            ),
            new EventResponse(2L,
                "test2",
                "/connectable-events/image_0xtest.jpeg",
                LocalDateTime.now(),
                "description2",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );

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
        given(eventService.getEventDetail(EVENT_ID)).willReturn(EVENT_DETAIL_RESPONSE);

        mockMvc.perform(get("/events/{eventId}", EVENT_ID)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.artistName").value("빅나티"))
            .andDo(print());
    }
}