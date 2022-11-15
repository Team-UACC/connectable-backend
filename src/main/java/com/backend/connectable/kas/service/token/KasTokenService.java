package com.backend.connectable.kas.service.token;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.kas.config.KasWebClient;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.common.endpoint.EndPointGenerator;
import com.backend.connectable.kas.service.token.dto.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KasTokenService {

    private static final String TOKEN_ID_PREFIX = "0x";

    private final KasWebClient kasWebClient;
    private final EndPointGenerator endPointGenerator;

    @Value("${kas.settings.account-pool-address}")
    private String accountPoolAddress;

    public TransactionResponse mintToken(
            String contractAddress, String tokenId, String tokenUri, String tokenOwner) {
        TokenRequest tokenRequest =
                TokenRequest.builder().id(tokenId).uri(tokenUri).to(tokenOwner).build();

        String url = endPointGenerator.tokenBaseUrl(contractAddress);
        Mono<TransactionResponse> response =
                kasWebClient.postForObject(url, tokenRequest, TransactionResponse.class);
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
        String url = endPointGenerator.tokenBaseUrl(contractAddress);
        Mono<TokensResponse> response = kasWebClient.getForObject(url, TokensResponse.class);
        return response.block();
    }

    public TokenResponse getToken(String contractAddress, String tokenId) {
        String url = endPointGenerator.tokenByTokenIdUrl(contractAddress, tokenId);
        Mono<TokenResponse> response = kasWebClient.getForObject(url, TokenResponse.class);
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

        String url = endPointGenerator.tokenByTokenIdUrl(contractAddress, tokenId);
        Mono<TransactionResponse> request =
                kasWebClient.postForObject(url, tokenRequest, TransactionResponse.class);
        return request.block();
    }

    public TransactionResponse sendMyToken(String contractAddress, int tokenId, String receiver) {
        return sendMyToken(
                contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId), receiver);
    }

    public TransactionResponse burnMyToken(String contractAddress, String tokenId) {
        TokenDeleteRequest tokenDeleteRequest =
                TokenDeleteRequest.builder().from(accountPoolAddress).build();

        String url = endPointGenerator.tokenByTokenIdUrl(contractAddress, tokenId);
        Mono<TransactionResponse> request =
                kasWebClient.deleteForObject(url, tokenDeleteRequest, TransactionResponse.class);
        return request.block();
    }

    public TransactionResponse burnMyToken(String contractAddress, int tokenId) {
        return burnMyToken(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, String tokenId) {
        String url = endPointGenerator.tokenByTokenIdHistoryUrl(contractAddress, tokenId);
        Mono<TokenHistoriesResponse> request =
                kasWebClient.getForObject(url, TokenHistoriesResponse.class);
        return request.block();
    }

    public TokenHistoriesResponse getTokenHistory(String contractAddress, int tokenId) {
        return getTokenHistory(contractAddress, TOKEN_ID_PREFIX + Integer.toHexString(tokenId));
    }

    public Map<String, TokensResponse> findAllTokensOwnedByUser(
            List<String> contractAddresses, String userKlaytnAddress) {
        Map<String, TokensResponse> tokensResponses = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(contractAddresses.size());

        for (String contractAddress : contractAddresses) {
            Mono<TokensResponse> tokensResponseMono =
                    findTokensOwnedByUser(contractAddress, userKlaytnAddress);
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
            Thread.currentThread().interrupt();
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ASYNC_HANDLING_ERROR);
        }
    }

    private Mono<TokensResponse> findTokensOwnedByUser(
            String contractAddress, String userKlaytnAddress) {
        String url = endPointGenerator.tokenByKlaytnAddressUrl(contractAddress, userKlaytnAddress);
        return kasWebClient.getForObject(url, TokensResponse.class);
    }

    public TokenIdentifierByKlaytnAddress findTokenHoldingStatus(
            List<String> contractAddresses, List<String> klaytnAddresses) {
        Map<String, TokensResponse> holderStatus = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch =
                new CountDownLatch(contractAddresses.size() * klaytnAddresses.size());

        for (String klaytnAddress : klaytnAddresses) {
            for (String contractAddress : contractAddresses) {
                Mono<TokensResponse> tokensResponseMono =
                        findTokensOwnedByUser(contractAddress, klaytnAddress);
                tokensResponseMono.subscribe(
                        tokensResponse -> {
                            if (tokensResponse.hasItem()) {
                                holderStatus.put(klaytnAddress, tokensResponse);
                            }
                            countDownLatch.countDown();
                        });
            }
        }

        try {
            countDownLatch.await();
            return TokenIdentifierByKlaytnAddress.of(holderStatus);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ASYNC_HANDLING_ERROR);
        }
    }
}
