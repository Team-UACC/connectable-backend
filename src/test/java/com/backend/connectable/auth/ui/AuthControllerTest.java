package com.backend.connectable.auth.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.connectable.auth.service.AuthService;
import com.backend.connectable.security.config.SecurityConfiguration;
import com.backend.connectable.security.custom.JwtAuthenticationFilter;
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

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = {SecurityConfiguration.class, JwtAuthenticationFilter.class})
        })
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest {

    private static final String PHONE_NUMBER = "010-2222-3333";
    private static final Long DURATION = 1L;
    private static final String AUTH_KEY = "123123";

    @Autowired private MockMvc mockMvc;

    @MockBean private AuthService authService;

    @DisplayName("비회원으로 인증키 요청시 문자열의 인증키를 받는데 성공한다.")
    @WithMockUser
    @Test
    void getAuthKeySuccess() throws Exception {
        // given & when
        given(authService.getAuthKey(PHONE_NUMBER, DURATION)).willReturn(AUTH_KEY);

        mockMvc.perform(
                        get(
                                        "/auth/sms/key?phoneNumber={phone-number}&duration={duration}",
                                        PHONE_NUMBER,
                                        DURATION)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(AUTH_KEY))
                .andDo(print());
    }

    @DisplayName("비회원으로 인증키 검증 성공시 boolean=true를 반환받는다.")
    @WithMockUser
    @Test
    void certifyAuthKeySuccess() throws Exception {
        // given & when
        boolean isCertified = true;
        given(authService.certifyKey(PHONE_NUMBER, AUTH_KEY)).willReturn(isCertified);

        // then
        mockMvc.perform(
                        get(
                                        "/auth/sms/certification?phoneNumber={phone-number}&authKey={authKey}",
                                        PHONE_NUMBER,
                                        AUTH_KEY)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
