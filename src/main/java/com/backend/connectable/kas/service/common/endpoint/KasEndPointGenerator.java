package com.backend.connectable.kas.service.common.endpoint;

import org.springframework.stereotype.Component;

@Component
public class KasEndPointGenerator implements EndPointGenerator {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";

    @Override
    public String contractBaseUrl() {
        return CONTRACT_API_URL;
    }

    @Override
    public String contractByContractAddressUrl(String contractAddress) {
        return CONTRACT_API_URL + "/" + contractAddress;
    }

    @Override
    public String contractByAliasUrl(String alias) {
        return CONTRACT_API_URL + "/" + alias;
    }

    @Override
    public String tokenBaseUrl(String contractAddress) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token";
    }

    @Override
    public String tokenByTokenIdUrl(String contractAddress, String tokenId) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId;
    }

    @Override
    public String tokenByTokenIdHistoryUrl(String contractAddress, String tokenId) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId + "/history";
    }

    @Override
    public String tokenByKlaytnAddressUrl(String contractAddress, String klaytnAddress) {
        return CONTRACT_API_URL + "/" + contractAddress + "/owner/" + klaytnAddress;
    }
}
