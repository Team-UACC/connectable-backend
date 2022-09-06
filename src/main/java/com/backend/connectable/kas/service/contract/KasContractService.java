package com.backend.connectable.kas.service.contract;

import com.backend.connectable.kas.config.KasWebClient;
import com.backend.connectable.kas.service.contract.dto.*;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KasContractService {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";

    private final KasWebClient kasWebClient;

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
    private void setTransactionOptionRequest() {
        if (enableGlobalFeePayer) {
            options = new TransactionOptionRequest(true);
        } else {
            options =
                    new TransactionOptionFeePayerRequest(
                            false,
                            new UserFeePayerOptionRequest(userFeePayerKrn, userFeePayerAddress));
        }
    }

    public ContractDeployResponse deployContract(
            String name, String symbol, String alias, String owner) {
        ContractDeployRequest contractDeployRequest =
                ContractDeployRequest.builder()
                        .name(name)
                        .symbol(symbol)
                        .alias(alias)
                        .owner(owner)
                        .options(options)
                        .build();

        Mono<ContractDeployResponse> response =
                kasWebClient.postForObject(
                        CONTRACT_API_URL, contractDeployRequest, ContractDeployResponse.class);
        return response.block();
    }

    public ContractDeployResponse deployMyContract(String name, String symbol, String alias) {
        return deployContract(name, symbol, alias, accountPoolAddress);
    }

    public ContractItemsResponse getMyContracts() {
        Mono<ContractItemsResponse> response =
                kasWebClient.getForObject(CONTRACT_API_URL, ContractItemsResponse.class);
        return response.block();
    }

    public ContractItemResponse getMyContract(String contractAddress) {
        Mono<ContractItemResponse> response =
                kasWebClient.getForObject(
                        CONTRACT_API_URL + "/" + contractAddress, ContractItemResponse.class);
        return response.block();
    }

    public ContractItemResponse getMyContractMyAlias(String alias) {
        Mono<ContractItemResponse> response =
                kasWebClient.getForObject(
                        CONTRACT_API_URL + "/" + alias, ContractItemResponse.class);
        return response.block();
    }
}
