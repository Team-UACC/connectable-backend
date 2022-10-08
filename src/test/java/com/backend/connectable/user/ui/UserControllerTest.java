package com.backend.connectable.user.ui;

import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.backend.connectable.user.ui.dto.UserLoginRequest;
import com.backend.connectable.user.ui.dto.UserModifyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @Autowired UserRepository userRepository;

    @BeforeEach
    void setUp() {
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

    @DisplayName("유저 로그인에 성공한다.")
    @Test
    void loginUserSuccess() throws Exception {
        // given & when
        UserLoginRequest userLoginRequest =
                new UserLoginRequest("95323c1e-c6a5-4a91-8f99-8593f7d70a4f");
        String json = objectMapper.writeValueAsString(userLoginRequest);

        mockMvc.perform(post("/users/login").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저 조회 요청시 단일 유저 조회에 성공한다.")
    @WithUserDetails("0x1234")
    @Test
    void getUserSuccess() throws Exception {
        mockMvc.perform(get("/users").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저 정보 수정 요청시 성공한다.")
    @WithUserDetails("0x1234")
    @Test
    void modifyUserSuccess() throws Exception {
        // given & when
        UserModifyRequest userModifyRequest = new UserModifyRequest("esoo", "010-7777-9999");
        String json = objectMapper.writeValueAsString(userModifyRequest);

        // expected
        mockMvc.perform(put("/users").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저 닉네임 검증 요청에 성공한다.")
    @Test
    void validateNicknameSuccess() throws Exception {
        mockMvc.perform(get("/users/validation").param("nickname", "non-exist-nickname"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저 닉네임 중복 존재시 available=false 반환한다.")
    @Test
    void validateNicknameFail() throws Exception {
        mockMvc.perform(get("/users/validation").param("nickname", "mrlee7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value("false"))
                .andDo(print());
    }
}
