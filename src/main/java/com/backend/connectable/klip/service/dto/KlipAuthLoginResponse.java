package com.backend.connectable.klip.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KlipAuthLoginResponse {

    private String status;
    private String klaytnAddress;

    public static KlipAuthLoginResponse ofPrepared() {
        return new KlipAuthLoginResponse("prepared", "");
    }

    public static KlipAuthLoginResponse ofCompleted(String klaytnAddress) {
        return new KlipAuthLoginResponse("completed", klaytnAddress);
    }

    public static KlipAuthLoginResponse ofFailed() {
        return new KlipAuthLoginResponse("failed", "");
    }

    public boolean isPrepared() {
        return "prepared".equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return "failed".equalsIgnoreCase(status);
    }
}
