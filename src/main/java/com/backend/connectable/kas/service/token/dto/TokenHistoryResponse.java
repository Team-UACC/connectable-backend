package com.backend.connectable.kas.service.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenHistoryResponse {
    private String from;
    private String timestamp;
    private String to;
}
