package com.backend.connectable.klip.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.backend.connectable.klip.config.KlipRestTemplate;
import com.backend.connectable.klip.service.dto.KlipAuthHandleKlaytnAddressResponse;
import com.backend.connectable.klip.service.dto.KlipAuthHandleResponse;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;

@SpringBootTest
class KlipServiceTest {

    private static final String klaytnAddress = "0x1234";

    private static final KlipAuthHandleResponse preparedAuthHandleResponse =
            new KlipAuthHandleResponse(
                    "cb4cb3a6-1695-437f-98ef-66aadc9ab46c", "prepared", "1664330074", null);

    private static final KlipAuthHandleResponse completedAuthHandleResponse =
            new KlipAuthHandleResponse(
                    "9d3fa8c4-740d-4617-99fe-f7f54a4d1b91",
                    "completed",
                    "1664330178",
                    new KlipAuthHandleKlaytnAddressResponse(klaytnAddress));

    @Autowired KlipService klipService;

    @MockBean KlipRestTemplate klipRestTemplate;

    @DisplayName("해당 requestKey를 통해 요청시 준비중이라면, status Prepared를 응답한다.")
    @Test
    void authLoginPrepared() {
        // given
        given(klipRestTemplate.getForEntity(any(), eq(KlipAuthHandleResponse.class)))
                .willReturn(preparedAuthHandleResponse);

        // when
        KlipAuthLoginResponse klipAuthLoginResponse = klipService.authLogin("prepared");

        // then
        assertThat(klipAuthLoginResponse.getKlaytnAddress()).isEmpty();
        assertThat(klipAuthLoginResponse.getStatus()).isEqualTo("prepared");
    }

    @DisplayName("해당 requestKey를 통해 요청 시 완료되었다면, status Completed를 응답한다.")
    @Test
    void authLoginCompleted() {
        // given
        given(klipRestTemplate.getForEntity(any(), eq(KlipAuthHandleResponse.class)))
                .willReturn(completedAuthHandleResponse);

        // when
        KlipAuthLoginResponse klipAuthLoginResponse = klipService.authLogin("completed");

        // then
        assertThat(klipAuthLoginResponse.getKlaytnAddress()).isEqualTo(klaytnAddress);
        assertThat(klipAuthLoginResponse.getStatus()).isEqualTo("completed");
    }

    @DisplayName("해당 requestKey를 통해 요청 시 실패하였다면, status Failed를 응답한다.")
    @Test
    void authLoginFailed() {
        // given
        given(klipRestTemplate.getForEntity(any(), eq(KlipAuthHandleResponse.class)))
                .willThrow(new RestClientException("failed!"));

        // when
        KlipAuthLoginResponse klipAuthLoginResponse = klipService.authLogin("failed");

        // then
        assertThat(klipAuthLoginResponse.getKlaytnAddress()).isEmpty();
        assertThat(klipAuthLoginResponse.getStatus()).isEqualTo("failed");
    }
}
