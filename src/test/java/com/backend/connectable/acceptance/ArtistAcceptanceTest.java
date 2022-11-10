package com.backend.connectable.acceptance;

import static com.backend.connectable.fixture.ArtistFixture.createArtistBigNaughty;
import static com.backend.connectable.fixture.EventFixture.createEventValidContractAddressMockedKas;
import static com.backend.connectable.fixture.UserFixture.createUserJoel;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.ui.dto.ArtistNftHolderResponse;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.kas.service.mockserver.KasServiceMockSetup;
import com.backend.connectable.security.custom.JwtProvider;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtistAcceptanceTest extends KasServiceMockSetup {

    @LocalServerPort public int port;

    @Autowired private DatabaseCleanUp databaseCleanUp;

    @Autowired protected UserRepository userRepository;

    @Autowired private JwtProvider jwtProvider;

    @Autowired protected ArtistRepository artistRepository;

    @Autowired protected EventRepository eventRepository;

    User joel = createUserJoel();

    Artist bigNaughty = createArtistBigNaughty();
    Event bigNaughtyEvent = createEventValidContractAddressMockedKas(bigNaughty);

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
        userRepository.save(joel);
        artistRepository.save(bigNaughty);
        eventRepository.save(bigNaughtyEvent);
    }

    @DisplayName("로그인 한 사용자는 본인이 NFT 홀더인지 검사할 수 있다.")
    @Test
    void isNftHolder() {
        // when
        ExtractableResponse<Response> response = 로그인_유저_NFT_홀더_검사(bigNaughty.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ArtistNftHolderResponse responseBody = response.body().as(ArtistNftHolderResponse.class);
        assertThat(responseBody.getIsNftHolder()).isTrue();
    }

    @DisplayName("로그인 안한 사용자는 본인이 NFT 홀더인지 검사할 수 없다.")
    @Test
    void isNftHolderNotLoggedIn() {
        // when
        ExtractableResponse<Response> response = 로그인_안된_유저_NFT_홀더_검사(bigNaughty.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 로그인_유저_NFT_홀더_검사(Long artistId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .auth()
                .oauth2(generateUserToken())
                .get("/artists/{artist-id}/owner", artistId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_안된_유저_NFT_홀더_검사(Long artistId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/artists/{artist-id}/owner", artistId)
                .then()
                .log()
                .all()
                .extract();
    }

    private String generateUserToken() {
        return jwtProvider.generateToken(joel.getKlaytnAddress());
    }
}
