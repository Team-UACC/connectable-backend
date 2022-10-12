package com.backend.connectable.acceptance;

import static com.backend.connectable.fixture.KlipFixture.getRequestKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.user.ui.dto.UserLoginRequest;
import com.backend.connectable.user.ui.dto.UserLoginSuccessResponse;
import com.backend.connectable.user.ui.dto.UserModifyRequest;
import com.backend.connectable.user.ui.dto.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAcceptanceTest {

    @LocalServerPort public int port;

    @MockBean protected KlipService klipService;

    private String requestKey;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        RestAssured.port = port;

        requestKey = getRequestKey();
        KlipAuthLoginResponse klipAuthLoginResponse =
                KlipAuthLoginResponse.ofCompleted("0x9f3c619b6f534d123c8fc35607d73eee0161da7b");
        given(klipService.authLogin(requestKey)).willReturn(klipAuthLoginResponse);
    }

    @DisplayName("requestKey를 이용하여 유저 로그인 시 유저 정보가 등록된다.")
    @Test
    void successUserLogin() {
        // given
        UserLoginRequest 유저_가입_요청 = new UserLoginRequest(requestKey);

        // when
        ExtractableResponse<Response> 유저_등록_성공후_응답 = 유저정보를_등록한다(유저_가입_요청);

        // then
        assertThat(유저_등록_성공후_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("자신의 프로필을 수정함으로 회원가입을 완료한다.")
    @Test
    void successUpdateMyProfile() {
        // given
        UserLoginRequest 유저_가입_요청 = new UserLoginRequest(requestKey);
        UserModifyRequest 유저_수정_요청 = new UserModifyRequest("mrlee7", "010-8516-1399");

        // when
        ExtractableResponse<Response> 유저_등록_성공후_응답 = 유저정보를_등록한다(유저_가입_요청);
        UserLoginSuccessResponse userLoginSuccessResponse =
                유저_등록_성공후_응답.as(UserLoginSuccessResponse.class);
        String 등록된_유저_토큰 = userLoginSuccessResponse.getJwt();

        ExtractableResponse<Response> 유저_수정_성공후_응답 = 유저정보를_수정한다(유저_수정_요청, 등록된_유저_토큰);
        ExtractableResponse<Response> 유저_조회_요청_성공_응답 = 유저정보를_조회한다(등록된_유저_토큰);
        UserResponse userResponse = 유저_조회_요청_성공_응답.as(UserResponse.class);

        // then
        assertThat(유저_수정_성공후_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(userResponse.getNickname()).isEqualTo("mrlee7");
        assertThat(userResponse.getPhoneNumber()).isEqualTo("010-8516-1399");
    }

    @DisplayName("닉네임 사용가능 여부를 조회한다.")
    @Test
    void validateNickNameAvailable() {
        // given
        String 존재하지_않는_닉네임 = "가나다라마바사아자차카타파하_!";

        // when
        ExtractableResponse<Response> 가입가능한_유저_닉네임 = 사용가능한_닉네임_여부_조회한다(존재하지_않는_닉네임);

        // then
        assertThat(가입가능한_유저_닉네임.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> 유저정보를_등록한다(UserLoginRequest userLoginRequest) {
        return RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(userLoginRequest)
                .when()
                .post("/users/login")
                .then()
                .log()
                .all()
                .extract();
    }

    private static ExtractableResponse<Response> 유저정보를_수정한다(
            UserModifyRequest userModifyRequest, String token) {
        return RestAssured.given()
                .log()
                .all()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(userModifyRequest)
                .when()
                .put("/users")
                .then()
                .log()
                .all()
                .extract();
    }

    private static ExtractableResponse<Response> 유저정보를_조회한다(String token) {
        return RestAssured.given()
                .log()
                .all()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get("/users")
                .then()
                .log()
                .all()
                .extract();
    }

    private static ExtractableResponse<Response> 사용가능한_닉네임_여부_조회한다(String nickname) {
        return RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .param("nickname", nickname)
                .when()
                .get("/users/validation")
                .then()
                .log()
                .all()
                .extract();
    }
}
