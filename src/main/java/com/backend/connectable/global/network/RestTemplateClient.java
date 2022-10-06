package com.backend.connectable.global.network;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestTemplateClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> T getForObject(String url, Class<T> responseType) {
        try {
            ResponseEntity<T> entity = restTemplate.getForEntity(url, responseType);
            return entity.getBody();
        } catch (RestClientException e) {
            log.error("$$ REST-TEMPLATE NETWORK ERROR $$");
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.NETWORK_ERROR);
        }
    }
}
