package com.backend.connectable.klip.config;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KlipRestTemplate {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> T getForEntity(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType).getBody();
    }
}
