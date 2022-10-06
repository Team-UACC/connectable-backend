package com.backend.connectable.klip.service;

import com.backend.connectable.global.network.RestTemplateClient;
import com.backend.connectable.klip.service.dto.KlipAuthHandleResponse;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class KlipService {

    private static final String KLIP_AUTH_LOGIN_URL =
            "https://a2a-api.klipwallet.com/v2/a2a/result?request_key=";

    private final RestTemplateClient restTemplateClient;

    public KlipAuthLoginResponse authLogin(String requestKey) {
        String requestUrl = KLIP_AUTH_LOGIN_URL + requestKey;
        try {
            KlipAuthHandleResponse klipAuthHandleResponse =
                    restTemplateClient.getForObject(requestUrl, KlipAuthHandleResponse.class);
            if (klipAuthHandleResponse.isPrepared()) {
                return KlipAuthLoginResponse.ofPrepared();
            }
            String klaytnAddress =
                    klipAuthHandleResponse.getResult().getKlaytn_address().toLowerCase();
            return KlipAuthLoginResponse.ofCompleted(klaytnAddress);
        } catch (RestClientException | NullPointerException e) {
            return KlipAuthLoginResponse.ofFailed();
        }
    }
}
