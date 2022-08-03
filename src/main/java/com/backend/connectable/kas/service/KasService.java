package com.backend.connectable.kas.service;

import com.backend.connectable.exception.KasException;
import com.backend.connectable.exception.KasExceptionResponse;
import com.backend.connectable.kas.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class KasService {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";
    private static final String TOKEN_ID_PREFIX = "0x";

    @Value("${kas.settings.enable-global-fee-payer}")
    private Boolean enableGlobalFeePayer;

    @Value("${kas.settings.account-pool-krn}")
    private String accountPoolKrn;

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    @Value("${kas.settings.user-fee-payer-krn}")
    private String userFeePayerKrn;

    @Value("${kas.settings.user-fee-payer-address}")
    private String userFeePayerAddress;

    @Value("${kas.settings.access-key-id}")
    private String accessKeyId;

    @Value("${kas.settings.secret-access-key}")
    private String secretAccessKey;

    @Value("${kas.settings.chain-id}")
    private String chainId;

    private WebClient webClient;
    private TransactionOptionRequest options;

    @PostConstruct
    private void setRequestOptions() {
        webClient = WebClient.builder()
            .defaultHeaders(headers -> {
                headers.add("x-chain-id", chainId);
                headers.add("Content-Type", "application/json");
                headers.add("Authorization",  Credentials.basic(accessKeyId, secretAccessKey));
                headers.add("x-krn", accountPoolKrn);
            })
            .build();

        if (enableGlobalFeePayer) {
            options = new TransactionOptionRequest(true);
        } else {
            options = new TransactionOptionFeePayerRequest(false,
                new UserFeePayerOptionRequest(userFeePayerKrn, userFeePayerAddress));
        }
    }

    public ContractDeployResponse deployContract(String name, String symbol, String alias, String owner) {
        ContractDeployRequest contractDeployRequest = ContractDeployRequest.builder()
            .name(name)
            .symbol(symbol)
            .alias(alias)
            .owner(owner)
            .options(options)
            .build();

        Object responseObject = webClient.post()
            .uri(CONTRACT_API_URL)
            .bodyValue(contractDeployRequest)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(ContractDeployResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (ContractDeployResponse) responseObject;
    }

    public ContractDeployResponse deployMyContract(String name, String symbol, String alias) {
        return deployContract(name, symbol, alias, accountPoolAddress);
    }

    public ContractItemsResponse getMyContracts() {
        Object responseObject = webClient.get()
            .uri(CONTRACT_API_URL)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(ContractItemsResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (ContractItemsResponse) responseObject;
    }

    public ContractItemResponse getMyContract(String contractAddress) {
        Object responseObject = webClient.get()
            .uri(CONTRACT_API_URL + "/" + contractAddress)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(ContractItemResponse.class);
                }
                return response.bodyToMono(KasExceptionResponse.class);
            })
            .block();

        handleKasException(responseObject);
        return (ContractItemResponse) responseObject;
    }

    public TransactionResponse mintToken(String contractAddress, String tokenId, String tokenUri, String tokenOwner) {
        TokenRequest tokenRequest = TokenRequest.builder()
            .id(tokenId)
            .uri(tokenUri)
            .to(tokenOwner)
            .build();

        Object responseObject = webClient.post()
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
        return mintToken(contractAddress, TOKEN_ID_PREFIX + tokenId, tokenUri, accountPoolAddress);
    }

    public TokensResponse getTokens(String contractAddress) {
        Object responseObject = webClient.get()
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
        Object responseObject = webClient.get()
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
        return getToken(contractAddress, TOKEN_ID_PREFIX + tokenId);
    }

    public TransactionResponse sendMyToken(String contractAddress, String tokenId, String receiver) {
        TokenSendRequest tokenRequest = TokenSendRequest.builder()
            .sender(accountPoolAddress)
            .owner(accountPoolAddress)
            .to(receiver)
            .build();

        Object responseObject = webClient.post()
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
        return sendMyToken(contractAddress, TOKEN_ID_PREFIX + tokenId, receiver);
    }

    public TransactionResponse burnMyToken(String contractAddress, String tokenId) {
        TokenDeleteRequest tokenDeleteRequest = TokenDeleteRequest.builder()
            .from(accountPoolAddress)
            .build();

        Object responseObject = webClient.method(HttpMethod.DELETE)
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
        return burnMyToken(contractAddress, TOKEN_ID_PREFIX + tokenId);
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, String tokenId) {
        Object responseObject = webClient.get()
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
        return getTokenHistory(contractAddress, TOKEN_ID_PREFIX + tokenId);
    }

    private void handleKasException(Object responseObject) {
        if (responseObject instanceof KasExceptionResponse) {
            KasExceptionResponse kasExceptionResponse = (KasExceptionResponse) responseObject;
            log.error("KAS Request ID: " + kasExceptionResponse.getRequestId());
            log.error("KAS Code: " + kasExceptionResponse.getCode());
            log.error("KAS Message: " + kasExceptionResponse.getMessage());
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
        } catch (InterruptedException | KasException e) {
            throw new IllegalArgumentException("비동기 처리에서 문제가 발생했습니다.");
        }
    }

    private Mono<TokensResponse> findAllTokensOfContractAddressOwnedByUser(String contractAddress, String userKlaytnAddress) {
        return webClient.get()
            .uri(CONTRACT_API_URL + "/" + contractAddress + "/owner/" + userKlaytnAddress)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, response -> {
                throw Objects.requireNonNull(response.bodyToMono(KasException.class).block());
            })
            .bodyToMono(TokensResponse.class);
    }
}
