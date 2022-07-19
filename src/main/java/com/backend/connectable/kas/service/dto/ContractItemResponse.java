package com.backend.connectable.kas.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractItemResponse {
    private String address;
    private String alias;
    private String chainId;
    private String name;
    private String symbol;
}
