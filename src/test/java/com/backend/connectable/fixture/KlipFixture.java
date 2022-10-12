package com.backend.connectable.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class KlipFixture {

    private KlipFixture() {}

    public static String getRequestKey() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        RequestKeyRequest requestKeyRequest = new RequestKeyRequest("CONNECTABLE", "auth");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(requestKeyRequest), headers);

        RequestKeyResponse requestKeyResponse =
                restTemplate.postForObject(
                        "https://a2a-api.klipwallet.com/v2/a2a/prepare",
                        request,
                        RequestKeyResponse.class);

        return requestKeyResponse.getRequestKey();
    }
}
