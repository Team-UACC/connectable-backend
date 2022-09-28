package com.backend.connectable.acceptance;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthAcceptanceTest {

    private static final String 인증_요청_핸드폰_번호1 = "010-1234-1234";
    private static final Long 인증_요청_유효_기간 = 1L;

    @LocalServerPort public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("요청하여 응답받은 인증키를 이용하여 인증시 성공한다")
    @Test
    void successCertification() {
        // given & when
        String 응답받은_인증키 = 인증키를_요청한다();
        Boolean 요청_성공_여부 = 인증키를_검증한다(응답받은_인증키);

        // then
        assertThat(요청_성공_여부).isTrue();
    }

    private static String 인증키를_요청한다() {
        return RestAssured.given()
                .log()
                .all()
                .contentType("text/plain")
                .param("phoneNumber", 인증_요청_핸드폰_번호1)
                .param("duration", 인증_요청_유효_기간)
                .when()
                .get("/auth/sms/key")
                .then()
                .log()
                .all()
                .extract()
                .body()
                .asString();
    }

    private static Boolean 인증키를_검증한다(String 인증키) {
        return RestAssured.given()
                .log()
                .all()
                .contentType("text/plain")
                .param("phoneNumber", 인증_요청_핸드폰_번호1)
                .param("authKey", 인증키)
                .when()
                .get("/auth/sms/certification")
                .then()
                .log()
                .all()
                .extract()
                .response()
                .as(Boolean.class);
    }
}
