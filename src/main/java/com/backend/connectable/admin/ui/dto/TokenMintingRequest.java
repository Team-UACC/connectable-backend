package com.backend.connectable.admin.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenMintingRequest {

    private String contractAddress;
    private int startTokenId;
    private int endTokenId;
    private String tokenUri;
    private int price;
}
