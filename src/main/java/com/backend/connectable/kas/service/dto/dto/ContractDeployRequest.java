package com.backend.connectable.kas.service.dto.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDeployRequest {
    private String alias;
    private String symbol;
    private String name;
    private String owner;
    private TransactionOptionRequest options;
}
