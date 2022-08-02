package com.backend.connectable.klip.service;

import com.backend.connectable.klip.service.dto.KlipAuthHandleResponse;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class KlipService {

    private static final String KLIP_AUTH_LOGIN_URL = "https://a2a-api.klipwallet.com/v2/a2a/result?request_key=";

    private final RestTemplate restTemplate = new RestTemplate();

    public KlipAuthLoginResponse authLogin(String requestKey) {
        String requestUrl = KLIP_AUTH_LOGIN_URL + requestKey;
        try {
            KlipAuthHandleResponse klipAuthHandleResponse =
                    restTemplate.getForEntity(requestUrl, KlipAuthHandleResponse.class).getBody();
            if (klipAuthHandleResponse.isPrepared()) {
                return KlipAuthLoginResponse.ofPrepared();
            }
            String klaytnAddress = klipAuthHandleResponse.getResult()
                    .getKlaytn_address()
                    .toLowerCase();
            return KlipAuthLoginResponse.ofCompleted(klaytnAddress);
        } catch (RestClientException | NullPointerException e) {
            return KlipAuthLoginResponse.ofFailed();
        }
    }
}
