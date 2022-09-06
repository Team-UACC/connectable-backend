package com.backend.connectable.kas.config;

import okhttp3.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KasWebClientConfiguration {

    @Value("${kas.settings.account-pool-krn}")
    private String accountPoolKrn;

    @Value("${kas.settings.access-key-id}")
    private String accessKeyId;

    @Value("${kas.settings.secret-access-key}")
    private String secretAccessKey;

    @Value("${kas.settings.chain-id}")
    private String chainId;

    @Bean
    public WebClient kasWebClient() {
        return WebClient.builder()
            .defaultHeaders(headers -> {
                headers.add("x-chain-id", chainId);
                headers.add("Content-Type", "application/json");
                headers.add("Authorization",  Credentials.basic(accessKeyId, secretAccessKey));
                headers.add("x-krn", accountPoolKrn);
            })
            .build();
    }
}
