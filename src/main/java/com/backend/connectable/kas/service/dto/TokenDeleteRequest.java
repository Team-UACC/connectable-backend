package com.backend.connectable.kas.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDeleteRequest {
    private String from;

    @Builder
    public TokenDeleteRequest(String from) {
        this.from = from;
    }
}
