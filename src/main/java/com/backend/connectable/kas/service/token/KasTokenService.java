package com.backend.connectable.kas.service.token;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.kas.config.KasWebClient;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.token.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
public class KasTokenService {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";
    private static final String TOKEN_ID_PREFIX = "0x";

    private final KasWebClient kasWebClient;

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    public TransactionResponse mintToken(
            String contractAddress, String tokenId, String tokenUri, String tokenOwner) {
        TokenRequest tokenRequest =
                TokenRequest.builder().id(tokenId).uri(tokenUri).to(tokenOwner).build();

        Mono<TransactionResponse> response = kasWebClient.postForObject(CONTRACT_API_URL + "/" + contractAddress + "/token", tokenRequest, TransactionResponse.class);
        return response.block();
    }

    public TransactionResponse mintMyToken(
            String contractAddress, String tokenId, String tokenUri) {
        return mintToken(contractAddress, tokenId, tokenUri, accountPoolAddress);
    }

    public TransactionResponse mintMyToken(String contractAddress, int tokenId, String tokenUri) {
        return mintToken(
                contractAddress,
                TOKEN_ID_PREFIX + Integer.toHexString(tokenId),
                tokenUri,
                accountPoolAddress);
    }

    public TokensResponse getTokens(String contractAddress) {
        Mono<TokensResponse> response = kasWebClient.getForObject(CONTRACT_API_URL + "/" + contractAddress + "/token", TokensResponse.class);
        return response.block();
    }

    public TokenResponse getToken(String contractAddress, String tokenId) {
        Mono<TokenResponse> response = kasWebClient.getForObject(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId, TokenResponse.class);
        return response.block();
    }

    public TokenResponse getToken(String contractAddress, int tokenId) {
        return getToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public TransactionResponse sendMyToken(
            String contractAddress, String tokenId, String receiver) {
        TokenSendRequest tokenRequest =
                TokenSendRequest.builder()
                        .sender(accountPoolAddress)
                        .owner(accountPoolAddress)
                        .to(receiver)
                        .build();

        Mono<TransactionResponse> request = kasWebClient.postForObject(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId, tokenRequest, TransactionResponse.class);
        return request.block();
    }

    public TransactionResponse sendMyToken(String contractAddress, int tokenId, String receiver) {
        return sendMyToken(
                contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId), receiver);
    }

    public TransactionResponse burnMyToken(String contractAddress, String tokenId) {
        TokenDeleteRequest tokenDeleteRequest =
                TokenDeleteRequest.builder().from(accountPoolAddress).build();

        Mono<TransactionResponse> request = kasWebClient.deleteForObject(CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId, tokenDeleteRequest, TransactionResponse.class);
        return request.block();
    }

    public TransactionResponse burnMyToken(String contractAddress, int tokenId) {
        return burnMyToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, String tokenId) {
        Mono<TokenHistoriesResponse> request = kasWebClient.getForObject(CONTRACT_API_URL
            + "/"
            + contractAddress
            + "/token/"
            + tokenId
            + "/history", TokenHistoriesResponse.class);
        return request.block();
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, int tokenId) {
        return getTokenHistory(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public Map<String, TokensResponse> findAllTokensOfContractAddressesOwnedByUser(
            List<String> contractAddresses, String userKlaytnAddress) {
        Map<String, TokensResponse> tokensResponses = new HashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(contractAddresses.size());

        for (String contractAddress : contractAddresses) {
            Mono<TokensResponse> tokensResponseMono =
                kasWebClient.getForObject(CONTRACT_API_URL + "/" + contractAddress + "/owner/" + userKlaytnAddress, TokensResponse.class);
            tokensResponseMono.subscribe(
                    tokensResponse -> {
                        tokensResponses.put(contractAddress, tokensResponse);
                        countDownLatch.countDown();
                    });
        }

        try {
            countDownLatch.await();
            return tokensResponses;
        } catch (InterruptedException e) {
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ASYNC_HANDLING_ERROR);
        }
    }
}
