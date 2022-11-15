package com.backend.connectable.kas.service.token.dto;

import static com.backend.connectable.exception.ErrorType.UNEXPECTED_SERVER_ERROR;

import com.backend.connectable.exception.ConnectableException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class TokenIdentifierByKlaytnAddress {

    private Map<String, TokenIdentifier> tokenHolderStatus;

    public static TokenIdentifierByKlaytnAddress of(Map<String, TokensResponse> holderStatus) {
        Map<String, TokenIdentifier> tokenHolderStatus =
                holderStatus.entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> entry.getValue().getFirstTokenIdentifier()));
        return new TokenIdentifierByKlaytnAddress(tokenHolderStatus);
    }

    public boolean isHolder(String klaytnAddress) {
        return tokenHolderStatus.containsKey(klaytnAddress);
    }

    public TokenIdentifier getTokenIdentifier(String klaytnAddress) {
        if (!isHolder(klaytnAddress)) {
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_SERVER_ERROR);
        }
        return tokenHolderStatus.get(klaytnAddress);
    }
}
