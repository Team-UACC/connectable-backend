package com.backend.connectable.kas.service.dto.dto;

import lombok.*;

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
