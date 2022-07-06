package com.backend.connectable.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("JwtProvider를 이용하여, jwt 토큰을 발행한다.")
    void generateToken() {
        // given
        String payload = "0x1234abcd";

        // when
        String token = jwtProvider.generateToken(payload);

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("생성된 jwt 토큰을 검증한다.")
    void verifyToken() {
        // given
        String payload = "0x1234abcd";
        String token = jwtProvider.generateToken(payload);

        // when
        Boolean result = jwtProvider.verify(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 jwt 토큰을 검증할 수 있다.")
    void verifyInvalidToken() {
        // given
        String token = "invalid.token.connectable";

        // when
        Boolean result = jwtProvider.verify(token);

        // then
        assertThat(result).isFalse();
    }
}