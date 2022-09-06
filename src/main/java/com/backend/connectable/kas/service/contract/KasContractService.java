package com.backend.connectable.kas.service.contract;

import com.backend.connectable.exception.KasException;
import com.backend.connectable.exception.KasExceptionResponse;
import com.backend.connectable.kas.service.contract.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class KasContractService {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";

    private final WebClient kasWebClient;

    @Value("${kas.settings.enable-global-fee-payer}")
    private Boolean enableGlobalFeePayer;

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    @Value("${kas.settings.user-fee-payer-krn}")
    private String userFeePayerKrn;

    @Value("${kas.settings.user-fee-payer-address}")
    private String userFeePayerAddress;

    private TransactionOptionRequest options;

    @PostConstruct
    private void setRequestOptions() {
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

        Object responseObject = kasWebClient.post()
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
        Object responseObject = kasWebClient.get()
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
        Object responseObject = kasWebClient.get()
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

    public ContractItemResponse getMyContractMyAlias(String alias) {
        Object responseObject = kasWebClient.get()
            .uri(CONTRACT_API_URL + "/" + alias)
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

    private void handleKasException(Object responseObject) {
        if (responseObject instanceof KasExceptionResponse) {
            KasExceptionResponse kasExceptionResponse = (KasExceptionResponse) responseObject;
            throw new KasException(kasExceptionResponse);
        }
    }
}
