package com.backend.connectable.kas.service.token.dto;

import static com.backend.connectable.exception.ErrorType.UNEXPECTED_SERVER_ERROR;

import com.backend.connectable.exception.ConnectableException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokensResponse {
    private String cursor;
    private List<TokenResponse> items;

    public List<String> getTokenUris() {
        return items.stream().map(TokenResponse::getTokenUri).collect(Collectors.toList());
    }

    public List<TokenIdentifier> getTokenIdentifiers() {
        return items.stream()
                .map(TokenResponse::generateTokenIdentifier)
                .collect(Collectors.toList());
    }

    public TokenIdentifier getFirstTokenIdentifier() {
        if (!hasItem()) {
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_SERVER_ERROR);
        }
        TokenResponse tokenResponse = items.get(0);
        return tokenResponse.generateTokenIdentifier();
    }

    public boolean hasItem() {
        return !items.isEmpty();
    }
}
