package com.backend.connectable.kas.service.contract;

import com.backend.connectable.kas.config.KasWebClient;
import com.backend.connectable.kas.service.common.endpoint.EndPointGenerator;
import com.backend.connectable.kas.service.contract.dto.ContractDeployRequest;
import com.backend.connectable.kas.service.contract.dto.ContractDeployResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KasContractService {

    private final KasWebClient kasWebClient;
    private final EndPointGenerator endPointGenerator;
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

        String url = endPointGenerator.contractBaseUrl();
        Mono<ContractDeployResponse> response =
                kasWebClient.postForObject(
                        url, contractDeployRequest, ContractDeployResponse.class);
        return response.block();
    }

    public ContractDeployResponse deployMyContract(String name, String symbol, String alias) {
        return deployContract(name, symbol, alias, accountPoolAddress);
    }

    public ContractItemsResponse getMyContracts() {
        String url = endPointGenerator.contractBaseUrl();
        Mono<ContractItemsResponse> response =
                kasWebClient.getForObject(url, ContractItemsResponse.class);
        return response.block();
    }

    public ContractItemResponse getMyContract(String contractAddress) {
        String url = endPointGenerator.contractByContractAddressUrl(contractAddress);
        Mono<ContractItemResponse> response =
                kasWebClient.getForObject(url, ContractItemResponse.class);
        return response.block();
    }

    public ContractItemResponse getMyContractByAlias(String alias) {
        String url = endPointGenerator.contractByAliasUrl(alias);
        Mono<ContractItemResponse> response =
                kasWebClient.getForObject(url, ContractItemResponse.class);
        return response.block();
    }
}
