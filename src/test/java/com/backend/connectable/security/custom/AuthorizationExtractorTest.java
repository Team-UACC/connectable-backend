package com.backend.connectable.security.custom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class AuthorizationExtractorTest {

    @DisplayName("HttpServletRequest의 Authorization 헤더에서 인증 정보가 없다면 추출시 Null을 반환한다")
    @Test
    void noTokenReturnNull() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        String extracted = AuthorizationExtractor.extract(request);

        // then
        assertThat(extracted).isNull();
    }

    @DisplayName("HttpServletRequest의 Authorization 헤더에서 토큰 정보를 추출할 수 있다.")
    @Test
    void extractToken() {
        // given
        String token = "hello-world";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // when
        String extractedToken = AuthorizationExtractor.extract(request);

        // then
        assertThat(extractedToken).isEqualTo(token);
    }

    @DisplayName("HttpServletRequest의 Authorization 헤더에서 토큰 정보가 , 로 이어져있다면 맨 앞의 단어만 토큰으로 취급한다.")
    @Test
    void extractTokenWithBlank() {
        // given
        String token = "hello,world,good";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // when
        String extractedToken = AuthorizationExtractor.extract(request);

        // then
        assertThat(extractedToken).isEqualTo("hello");
    }
}
