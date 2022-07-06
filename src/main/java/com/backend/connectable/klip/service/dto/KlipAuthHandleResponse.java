package com.backend.connectable.klip.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KlipAuthHandleResponse {
    private String request_key;
    private String status;
    private String expiration_time;
    private KlipAuthHandleKlaytnAddressResponse result;

    public boolean isPrepared() {
        return "prepared".equalsIgnoreCase(status);
    }
}
