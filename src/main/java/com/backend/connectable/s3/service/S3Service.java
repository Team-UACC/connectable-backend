package com.backend.connectable.s3.service;

import com.backend.connectable.event.ui.dto.TicketMetadataResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class S3Service {

    private final RestTemplate restTemplate = new RestTemplate();

    public TicketMetadataResponse fetchMetadata(String tokenUri) {
        return restTemplate.getForEntity(tokenUri, TicketMetadataResponse.class)
            .getBody();
    }
}
