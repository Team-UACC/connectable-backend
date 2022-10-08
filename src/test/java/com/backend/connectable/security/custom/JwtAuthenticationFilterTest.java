package com.backend.connectable.security.custom;

import com.backend.connectable.security.exception.ConnectableSecurityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class JwtAuthenticationFilterTest {

    @Autowired JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean JwtProvider jwtProvider;

    @DisplayName("HttpServletRequest에 토큰이 있고, Admin API 요청이 아니며, 알맞은 서비스 토큰 정보라면, 해당 필터를 넘어간다.")
    @Test
    void serviceTokenVerified() throws ConnectableSecurityException, ServletException, IOException {
        // given
        String token = "service.good.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI("/test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        doNothing().when(jwtProvider).verify(token);

        // when & then
        assertThatCode(
                        () ->
                                jwtAuthenticationFilter.doFilterInternal(
                                        request, response, filterChain))
                .doesNotThrowAnyException();
    }

    @DisplayName(
            "HttpServletRequest에 토큰이 있고, Admin API 요청이 아니며, 알맞지 않은 서비스 토큰 정보라면, 해당 필터에서 예외를 응답한다.")
    @Test
    void serviceTokenInvalid() throws ConnectableSecurityException, ServletException, IOException {
        // given
        String token = "service.bad.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI("/test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        doThrow(new ConnectableSecurityException("유효하지 않습니다")).when(jwtProvider).verify(token);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @DisplayName("HttpServletRequest에 토큰이 있고, Admin API 요청이고, 알맞은 어드민 토큰 정보라면, 해당 필터를 넘어간다.")
    @Test
    void adminTokenVerified() throws ConnectableSecurityException, ServletException, IOException {
        // given
        String token = "admin.good.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI("/admin/test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        doNothing().when(jwtProvider).verifyAdmin(token);

        // when & then
        assertThatCode(
                        () ->
                                jwtAuthenticationFilter.doFilterInternal(
                                        request, response, filterChain))
                .doesNotThrowAnyException();
    }

    @DisplayName(
            "HttpServletRequest에 토큰이 있고, Admin API 요청이며, 알맞지 않은 어드민 토큰 정보라면, 해당 필터에서 예외를 응답한다.")
    @Test
    void adminTokenInvalid() throws ConnectableSecurityException, ServletException, IOException {
        // given
        String token = "admin.bad.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        request.setRequestURI("/admin/test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        doThrow(new ConnectableSecurityException("유효하지 않습니다")).when(jwtProvider).verifyAdmin(token);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(403);
    }
}
