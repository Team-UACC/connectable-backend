package com.backend.connectable.kas.service.token.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenHistoriesResponse {
    private String cursor;
    private List<TokenHistoryResponse> items;
}
