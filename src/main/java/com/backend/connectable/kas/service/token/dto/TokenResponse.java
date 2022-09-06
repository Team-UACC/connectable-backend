package com.backend.connectable.kas.service.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String owner;
    private String previousOwner;
    private String tokenId;
    private String tokenUri;
    private String transactionHash;

    public TokenIdentifier generateTokenIdentifier() {
        return new TokenIdentifier(tokenId, tokenUri);
    }
}
