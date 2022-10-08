package com.backend.connectable.kas.service.contract.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractItemsResponse {
    private String cursor;
    private List<ContractItemResponse> items;
}
