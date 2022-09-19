package com.backend.connectable.kas.service.common.endpoint;

public interface EndPointGenerator {
    String contractBaseUrl();

    String contractByContractAddressUrl(String contractAddress);

    String contractByAliasUrl(String alias);

    String tokenBaseUrl(String contractAddress);

    String tokenByTokenIdUrl(String contractAddress, String tokenId);

    String tokenByTokenIdHistoryUrl(String contractAddress, String tokenId);

    String tokenByKlaytnAddressUrl(String contractAddress, String klaytnAddress);
}
