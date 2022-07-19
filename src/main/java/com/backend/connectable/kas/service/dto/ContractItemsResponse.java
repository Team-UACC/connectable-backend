package com.backend.connectable.kas.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractItemsResponse {
    private String cursor;
    private List<ContractItemResponse> items;
}
