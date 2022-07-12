package com.backend.connectable.event.ui;

import com.backend.connectable.event.service.EventService;
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
}