package com.backend.connectable.kas.service.contract;

import com.backend.connectable.kas.service.contract.dto.TransactionOptionFeePayerRequest;
import com.backend.connectable.kas.service.contract.dto.TransactionOptionRequest;
import com.backend.connectable.kas.service.contract.dto.UserFeePayerOptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TransactionOptionManager {

    @Value("${kas.settings.enable-global-fee-payer}")
    private Boolean enableGlobalFeePayer;

    @Value("${kas.settings.user-fee-payer-krn}")
    private String userFeePayerKrn;

    @Value("${kas.settings.user-fee-payer-address}")
    private String userFeePayerAddress;

    private TransactionOptionRequest transactionOption;

    @PostConstruct
    private void setTransactionOptionRequest() {
        if (enableGlobalFeePayer) {
            transactionOption = new TransactionOptionRequest(true);
        } else {
            transactionOption =
                    new TransactionOptionFeePayerRequest(
                            false,
                            new UserFeePayerOptionRequest(userFeePayerKrn, userFeePayerAddress));
        }
    }

    public TransactionOptionRequest getTransactionOption() {
        return transactionOption;
    }
}
