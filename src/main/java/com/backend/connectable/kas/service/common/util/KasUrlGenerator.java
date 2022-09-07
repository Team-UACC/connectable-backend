package com.backend.connectable.kas.service.common.util;

public class KasUrlGenerator {

    private static final String CONTRACT_API_URL = "https://kip17-api.klaytnapi.com/v2/contract";

    private KasUrlGenerator() {}

    public static String contractBaseUrl() {
        return CONTRACT_API_URL;
    }

    public static String contractByContractAddressUrl(String contractAddress) {
        return CONTRACT_API_URL + "/" + contractAddress;
    }

    public static String contractByAliasUrl(String alias) {
        return CONTRACT_API_URL + "/" + alias;
    }

    public static String tokenBaseUrl(String contractAddress) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token";
    }

    public static String tokenByTokenIdUrl(String contractAddress, String tokenId) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId;
    }

    public static String tokenByTokenIdHistoryUrl(String contractAddress, String tokenId) {
        return CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId + "/history";
    }

    public static String tokenByKlaytnAddressUrl(String contractAddress, String klaytnAddress) {
        return CONTRACT_API_URL + "/" + contractAddress + "/owner/" + klaytnAddress;
    }
}
