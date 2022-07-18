package com.backend.connectable.kas.service;

import com.backend.connectable.exception.KasException;
import com.backend.connectable.exception.KasExceptionResponse;
import com.backend.connectable.kas.service.dto.dto.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class KasService {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";

    @Value("${kas.settings.enable-global-fee-payer}")
    private Boolean enableGlobalFeePayer;

    @Value("${kas.settings.my-address}")
    private String myAddress;

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
        return deployContract(name, symbol, alias, myAddress);
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
        return mintToken(contractAddress, tokenId, tokenUri, myAddress);
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

    public TokenResponse getToken(String contractAddress, Long tokenId) {
        String tokenIdAsString = "0x" + Long.toString(tokenId);
        return getToken(contractAddress, tokenIdAsString);
    }

    public TransactionResponse sendMyToken(String contractAddress, String tokenId, String receiver) {
        TokenSendRequest tokenRequest = TokenSendRequest.builder()
                .sender(myAddress)
                .owner(myAddress)
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

    private void handleKasException(Object responseObject) {
        if (responseObject instanceof KasExceptionResponse) {
            KasExceptionResponse kasExceptionResponse = (KasExceptionResponse) responseObject;
            log.info(kasExceptionResponse.getRequestId());
            log.info(kasExceptionResponse.getCode());
            log.info(kasExceptionResponse.getMessage());
            throw new KasException(kasExceptionResponse);
        }
    }
}
