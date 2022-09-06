package com.backend.connectable.kas.service.token;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.exception.KasException;
import com.backend.connectable.exception.KasExceptionResponse;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.token.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
public class KasTokenService {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";
    private static final String TOKEN_ID_PREFIX = "0x";

    private final WebClient kasWebClient;

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    public TransactionResponse mintToken(String contractAddress, String tokenId, String tokenUri, String tokenOwner) {
        TokenRequest tokenRequest = TokenRequest.builder()
            .id(tokenId)
            .uri(tokenUri)
            .to(tokenOwner)
            .build();

        Object responseObject = kasWebClient.post()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/token")
            .bodyValue(tokenRequest)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TransactionResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (TransactionResponse) responseObject;
    }

    public TransactionResponse mintMyToken(String contractAddress, String tokenId, String tokenUri) {
        return mintToken(contractAddress, tokenId, tokenUri, accountPoolAddress);
    }

    public TransactionResponse mintMyToken(String contractAddress, int tokenId, String tokenUri) {
        return mintToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId), tokenUri, accountPoolAddress);
    }

    public TokensResponse getTokens(String contractAddress) {
        Object responseObject = kasWebClient.get()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/token")
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TokensResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (TokensResponse) responseObject;
    }

    public TokenResponse getToken(String contractAddress, String tokenId) {
        Object responseObject = kasWebClient.get()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TokenResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (TokenResponse) responseObject;
    }

    public TokenResponse getToken(String contractAddress, int tokenId) {
        return getToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public TransactionResponse sendMyToken(String contractAddress, String tokenId, String receiver) {
        TokenSendRequest tokenRequest = TokenSendRequest.builder()
            .sender(accountPoolAddress)
            .owner(accountPoolAddress)
            .to(receiver)
            .build();

        Object responseObject = kasWebClient.post()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId)
            .bodyValue(tokenRequest)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TransactionResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (TransactionResponse) responseObject;
    }

    public TransactionResponse sendMyToken(String contractAddress, int tokenId, String receiver) {
        return sendMyToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId), receiver);
    }

    public TransactionResponse burnMyToken(String contractAddress, String tokenId) {
        TokenDeleteRequest tokenDeleteRequest = TokenDeleteRequest.builder()
            .from(accountPoolAddress)
            .build();

        Object responseObject = kasWebClient.method(HttpMethod.DELETE)
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId)
            .bodyValue(tokenDeleteRequest)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TransactionResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (TransactionResponse) responseObject;
    }

    public TransactionResponse burnMyToken(String contractAddress, int tokenId) {
        return burnMyToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, String tokenId) {
        Object responseObject = kasWebClient.get()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId + "/history")
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(TokenHistoriesResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (TokenHistoriesResponse) responseObject;
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, int tokenId) {
        return getTokenHistory(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    private void handleKasException(Object responseObject) {
        if (responseObject instanceof KasExceptionResponse) {
            KasExceptionResponse kasExceptionResponse = (KasExceptionResponse) responseObject;
            throw new KasException(kasExceptionResponse);
        }
    }

    public Map<String, TokensResponse> findAllTokensOfContractAddressesOwnedByUser(List<String> contractAddresses, String userKlaytnAddress) {
        Map<String, TokensResponse> tokensResponses = new HashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(contractAddresses.size());

        for (String contractAddress : contractAddresses) {
            Mono<TokensResponse> tokensResponseMono = findAllTokensOfContractAddressOwnedByUser(contractAddress, userKlaytnAddress);
            tokensResponseMono.subscribe(tokensResponse -> {
                tokensResponses.put(contractAddress, tokensResponse);
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
            return tokensResponses;
        } catch (InterruptedException e) {
            throw new ConnectableException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ASYNC_HANDLING_ERROR);
        }
    }

    private Mono<TokensResponse> findAllTokensOfContractAddressOwnedByUser(String contractAddress, String userKlaytnAddress) {
        return kasWebClient.get()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/owner/" + userKlaytnAddress)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, response -> {
                throw Objects.requireNonNull(response.bodyToMono(KasException.class).block());
            })
            .bodyToMono(TokensResponse.class);
    }
}
