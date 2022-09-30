package com.backend.connectable.security.custom;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.backend.connectable.security.exception.ConnectableSecurityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtProviderTest {

    @Autowired JwtProvider jwtProvider;

    @Value("${jwt.admin-payload}")
    private String adminPayload;

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

        // when & then
        assertThatCode(() -> jwtProvider.verify(token)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효하지 않은 jwt 토큰을 검증할 수 있다.")
    void verifyInvalidToken() {
        // given
        String token = "invalid.token.connectable";

        // when & then
        assertThatThrownBy(() -> jwtProvider.verify(token))
                .isInstanceOf(ConnectableSecurityException.class);
    }

    @Test
    @DisplayName("생성된 jwt 토큰의 페이로드를 추출할 수 있다.")
    void exportClaim() throws ConnectableSecurityException {
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
                .isInstanceOf(ConnectableSecurityException.class);
    }

    @DisplayName("어드민 JWT 토큰에 대해 verify 할 수 있다.")
    @Test
    void verifyAdminToken() {
        // given
        String adminToken = jwtProvider.generateToken(adminPayload);

        // when & then
        assertThatCode(() -> jwtProvider.verifyAdmin(adminToken)).doesNotThrowAnyException();
    }

    @DisplayName("어드민 JWT 토큰이 아닌 대해 verify시 에러가 발생한다.")
    @Test
    void verifyAdminTokenThrowsException() {
        // given
        String adminToken = jwtProvider.generateToken("weird-payload");

        // when & then
        assertThatThrownBy(() -> jwtProvider.verifyAdmin(adminToken))
                .isInstanceOf(ConnectableSecurityException.class);
    }
}
