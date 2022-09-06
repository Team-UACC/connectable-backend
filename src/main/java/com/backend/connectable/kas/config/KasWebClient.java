package com.backend.connectable.kas.config;

import com.backend.connectable.exception.KasException;
import com.backend.connectable.exception.KasExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KasWebClient {

    private final WebClient kasWebClient;

    public <T> T getForObject(String url, Class<T> responseType) {
        Object responseObj = kasWebClient.get()
            .uri(url)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(responseType);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();
        handleKasException(responseObj);
        return (T) responseObj;
    }

    private void handleKasException(Object responseObject) {
        if (responseObject instanceof KasExceptionResponse) {
            KasExceptionResponse kasExceptionResponse = (KasExceptionResponse) responseObject;
            throw new KasException(kasExceptionResponse);
        }
    }
}
