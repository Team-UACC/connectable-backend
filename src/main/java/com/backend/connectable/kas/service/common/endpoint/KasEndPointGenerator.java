package com.backend.connectable.kas.service.common.endpoint;

import org.springframework.stereotype.Component;

@Component
public class KasEndPointGenerator implements EndPointGenerator {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";

    public String contractBaseUrl() {
        return CONTRACT_API_URL;
    }

    public String contractByContractAddressUrl(String contractAddress) {
        return CONTRACT_API_URL + "/" + contractAddress;
    }

    public String contractByAliasUrl(String alias) {
        return CONTRACT_API_URL + "/" + alias;
    }

    public String tokenBaseUrl(String contractAddress) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token";
    }

    public String tokenByTokenIdUrl(String contractAddress, String tokenId) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId;
    }

    public String tokenByTokenIdHistoryUrl(String contractAddress, String tokenId) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId + "/history";
    }

    public String tokenByKlaytnAddressUrl(String contractAddress, String klaytnAddress) {
        return CONTRACT_API_URL + "/" + contractAddress + "/owner/" + klaytnAddress;
    }
}
