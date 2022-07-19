package com.backend.connectable.kas.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenSendRequest {
    private String sender;
    private String owner;
    private String to;

    @Builder
    public TokenSendRequest(String sender, String owner, String to) {
        this.sender = sender;
        this.owner = owner;
        this.to = to;
    }
}
