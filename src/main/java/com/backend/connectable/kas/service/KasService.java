package com.backend.connectable.kas.service;

import com.backend.connectable.global.aop.TimeCheck;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.contract.KasContractService;
import com.backend.connectable.kas.service.contract.dto.ContractDeployResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemsResponse;
import com.backend.connectable.kas.service.token.KasTokenService;
import com.backend.connectable.kas.service.token.dto.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KasService {

    private final KasContractService kasContractService;
    private final KasTokenService kasTokenService;

    public ContractDeployResponse deployContract(
            String name, String symbol, String alias, String owner) {
        return kasContractService.deployContract(name, symbol, alias, owner);
    }

    public ContractDeployResponse deployMyContract(String name, String symbol, String alias) {
        return kasContractService.deployMyContract(name, symbol, alias);
    }

    public ContractItemsResponse getMyContracts() {
        return kasContractService.getMyContracts();
    }

    public ContractItemResponse getMyContract(String contractAddress) {
        return kasContractService.getMyContract(contractAddress);
    }

    public ContractItemResponse getMyContractByAlias(String alias) {
        return kasContractService.getMyContractByAlias(alias);
    }

    public TransactionResponse mintToken(
            String contractAddress, String tokenId, String tokenUri, String tokenOwner) {
        return kasTokenService.mintToken(contractAddress, tokenId, tokenUri, tokenOwner);
    }

    public TransactionResponse mintMyToken(
            String contractAddress, String tokenId, String tokenUri) {
        return kasTokenService.mintMyToken(contractAddress, tokenId, tokenUri);
    }

    public TransactionResponse mintMyToken(String contractAddress, int tokenId, String tokenUri) {
        return kasTokenService.mintMyToken(contractAddress, tokenId, tokenUri);
    }

    public TokensResponse getTokens(String contractAddress) {
        return kasTokenService.getTokens(contractAddress);
    }

    public TokenResponse getToken(String contractAddress, String tokenId) {
        return kasTokenService.getToken(contractAddress, tokenId);
    }

    public TokenResponse getToken(String contractAddress, int tokenId) {
        return kasTokenService.getToken(contractAddress, tokenId);
    }

    public TransactionResponse sendMyToken(
            String contractAddress, String tokenId, String receiver) {
        return kasTokenService.sendMyToken(contractAddress, tokenId, receiver);
    }

    public TransactionResponse sendMyToken(String contractAddress, int tokenId, String receiver) {
        return kasTokenService.sendMyToken(contractAddress, tokenId, receiver);
    }

    public TransactionResponse burnMyToken(String contractAddress, String tokenId) {
        return kasTokenService.burnMyToken(contractAddress, tokenId);
    }

    public TransactionResponse burnMyToken(String contractAddress, int tokenId) {
        return kasTokenService.burnMyToken(contractAddress, tokenId);
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, String tokenId) {
        return kasTokenService.getTokenHistory(contractAddress, tokenId);
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, int tokenId) {
        return kasTokenService.getTokenHistory(contractAddress, tokenId);
    }

    @TimeCheck
    public List<TokenIdentifier> findAllTokensOwnedByUser(
            List<String> contractAddresses, String userKlaytnAddress) {
        return kasTokenService
                .findAllTokensOwnedByUser(contractAddresses, userKlaytnAddress)
                .values()
                .stream()
                .map(TokensResponse::getTokenIdentifiers)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @TimeCheck
    public boolean checkIsTokenHolder(List<String> contractAddresses, String userKlaytnAddress) {
        return kasTokenService
                .findAllTokensOwnedByUser(contractAddresses, userKlaytnAddress)
                .values()
                .stream()
                .anyMatch(TokensResponse::hasItem);
    }

    @TimeCheck
    public TokenIdentifierByKlaytnAddress findTokenHoldingStatus(
            List<String> contractAddresses, List<String> klaytnAddresses) {
        return kasTokenService.findTokenHoldingStatus(contractAddresses, klaytnAddresses);
    }
}
