package com.backend.connectable.kas.service.contract;

import com.backend.connectable.kas.config.KasWebClient;
import com.backend.connectable.kas.service.common.util.KasUrlGenerator;
import com.backend.connectable.kas.service.contract.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KasContractService {

    private final KasWebClient kasWebClient;
    private final TransactionOptionManager transactionOptionManager;

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    public ContractDeployResponse deployContract(
            String name, String symbol, String alias, String owner) {
        ContractDeployRequest contractDeployRequest =
                ContractDeployRequest.builder()
                        .name(name)
                        .symbol(symbol)
                        .alias(alias)
                        .owner(owner)
                        .options(transactionOptionManager.getTransactionOption())
                        .build();

        String url = KasUrlGenerator.contractBaseUrl();
        Mono<ContractDeployResponse> response =
                kasWebClient.postForObject(
                        url, contractDeployRequest, ContractDeployResponse.class);
        return response.block();
    }

    public ContractDeployResponse deployMyContract(String name, String symbol, String alias) {
        return deployContract(name, symbol, alias, accountPoolAddress);
    }

    public ContractItemsResponse getMyContracts() {
        String url = KasUrlGenerator.contractBaseUrl();
        Mono<ContractItemsResponse> response =
                kasWebClient.getForObject(url, ContractItemsResponse.class);
        return response.block();
    }

    public ContractItemResponse getMyContract(String contractAddress) {
        String url = KasUrlGenerator.contractByContractAddressUrl(contractAddress);
        Mono<ContractItemResponse> response =
                kasWebClient.getForObject(url, ContractItemResponse.class);
        return response.block();
    }

    public ContractItemResponse getMyContractMyAlias(String alias) {
        String url = KasUrlGenerator.contractByAliasUrl(alias);
        Mono<ContractItemResponse> response =
                kasWebClient.getForObject(url, ContractItemResponse.class);
        return response.block();
    }
}
