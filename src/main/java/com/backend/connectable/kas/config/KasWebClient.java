package com.backend.connectable.kas.config;

import com.backend.connectable.exception.KasException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KasWebClient {

    private static final String TIMEOUT_EXCEPTION_MESSAGE = "WebClient Timeout";
    private static final String UNEXPECTED_RESPONSE_MESSAGE = "Expected response of ";

    private final WebClient kasWebClient;

    public <T> Mono<T> getForObject(String url, Class<T> responseType) {
        return kasWebClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> {
                            throw new KasException(
                                    url,
                                    UNEXPECTED_RESPONSE_MESSAGE + responseType.getSimpleName());
                        })
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(
                        TimeoutException.class,
                        exception -> {
                            throw new KasException(url, TIMEOUT_EXCEPTION_MESSAGE);
                        });
    }

    public <T> Mono<T> postForObject(String url, Object requestBody, Class<T> responseType) {
        return kasWebClient
                .post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> {
                            throw new KasException(
                                    url,
                                    UNEXPECTED_RESPONSE_MESSAGE + responseType.getSimpleName());
                        })
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(
                        TimeoutException.class,
                        exception -> {
                            throw new KasException(url, TIMEOUT_EXCEPTION_MESSAGE);
                        });
    }

    public <T> Mono<T> deleteForObject(String url, Object requestBody, Class<T> responseType) {
        return kasWebClient
                .method(HttpMethod.DELETE)
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> {
                            throw new KasException(
                                    url,
                                    UNEXPECTED_RESPONSE_MESSAGE + responseType.getSimpleName());
                        })
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(
                        TimeoutException.class,
                        exception -> {
                            throw new KasException(url, TIMEOUT_EXCEPTION_MESSAGE);
                        });
    }
}
