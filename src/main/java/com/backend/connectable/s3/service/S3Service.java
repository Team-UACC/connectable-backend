package com.backend.connectable.s3.service;

import com.backend.connectable.event.ui.dto.TicketMetadataResponse;
import com.backend.connectable.global.network.RestTemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final RestTemplateClient restTemplateClient;

    public TicketMetadataResponse fetchMetadata(String tokenUri) {
        return restTemplateClient.getForObject(tokenUri, TicketMetadataResponse.class);
    }
}
