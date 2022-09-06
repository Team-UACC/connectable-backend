package com.backend.connectable.kas.service.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractDeployRequest {
    private String alias;
    private String symbol;
    private String name;
    private String owner;
    private TransactionOptionRequest options;

    @Builder
    public ContractDeployRequest(String alias, String symbol, String name, String owner, TransactionOptionRequest options) {
        this.alias = alias;
        this.symbol = symbol;
        this.name = name;
        this.owner = owner;
        this.options = options;
    }
}
