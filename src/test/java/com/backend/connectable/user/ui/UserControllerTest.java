package com.backend.connectable.user.ui;

import com.backend.connectable.user.service.UserService;
import com.backend.connectable.user.ui.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("사용자의 정보를 가져올 수 있다.")
    @Test
    void getUser() throws Exception {
        // given
        UserResponse mockedUserResponse = new UserResponse("nickname", "phonenumber", "klaytnAddress");

        ObjectMapper mapper = new ObjectMapper();
        String mockedUserResponseAsString = mapper.writeValueAsString(mockedUserResponse);
        given(userService.getUserByWalletAddress(any())).willReturn(mockedUserResponse);

        // when & then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mockedUserResponseAsString));
    }
}