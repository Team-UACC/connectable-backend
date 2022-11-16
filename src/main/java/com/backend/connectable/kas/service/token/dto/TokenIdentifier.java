package com.backend.connectable.kas.service.token.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenIdentifier {
    private String tokenId;
    private String tokenUri;

    public String getTokenUri() {
        return tokenUri;
    }

    public int getTokenId() {
        return Integer.decode(tokenId);
    }
}
