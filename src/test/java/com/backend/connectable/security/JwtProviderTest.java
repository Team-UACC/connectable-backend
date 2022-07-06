package com.backend.connectable.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("JwtProvider를 이용하여, jwt 토큰을 발행한다.")
    void generateToken() {
        // given
        String claim = "0x1234abcd";

        // when
        String token = jwtProvider.generateToken(claim);

        // then
        System.out.println(token);
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("생성된 jwt 토큰을 검증한다.")
    void verifyToken() {
        // given
        String claim = "0x1234abcd";
        String token = jwtProvider.generateToken(claim);

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

    @Test
    @DisplayName("생성된 jwt 토큰의 페이로드를 추출할 수 있다.")
    void exportClaim() {
        // given
        String claim = "0x1234abcd";
        String token = jwtProvider.generateToken(claim);

        // when
        String resultPayload = jwtProvider.exportClaim(token);

        // then
        assertThat(resultPayload).isEqualTo(claim);
    }

    @Test
    @DisplayName("디코딩 할 수 없는 jwt 토큰이라면, 예외를 던진다.")
    void exportClaimWithException() {
        // given
        String invalidToken = "invalid.token.jwt";

        // when & then
        assertThatThrownBy(() -> jwtProvider.exportClaim(invalidToken))
            .isInstanceOf(IllegalArgumentException.class);
    }
}