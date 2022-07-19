package com.backend.connectable.kas.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionOptionFeePayerRequest extends TransactionOptionRequest {
    private UserFeePayerOptionRequest userFeePayer;

    public TransactionOptionFeePayerRequest(boolean enableGlobalFeePayer, UserFeePayerOptionRequest userFeePayer) {
        super(enableGlobalFeePayer);
        this.userFeePayer = userFeePayer;
    }
}
