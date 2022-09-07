package com.backend.connectable.kas.service.mockserver;

import static com.backend.connectable.kas.service.mockserver.KasServiceTestSetup.MOCK_SERVER_IP;
import static com.backend.connectable.kas.service.mockserver.KasServiceTestSetup.MOCK_SERVER_PORT;

import com.backend.connectable.kas.service.common.endpoint.EndPointGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class KasMockEndPointGenerator implements EndPointGenerator {

    private static final String TEST_CONTRACT_API_URL =
            "http://" + MOCK_SERVER_IP + ":" + MOCK_SERVER_PORT + "/contract";

    @Override
    public String contractBaseUrl() {
        return TEST_CONTRACT_API_URL;
    }

    @Override
    public String contractByContractAddressUrl(String contractAddress) {
        return TEST_CONTRACT_API_URL + "/" + contractAddress;
    }

    @Override
    public String contractByAliasUrl(String alias) {
        return TEST_CONTRACT_API_URL + "/" + alias;
    }

    @Override
    public String tokenBaseUrl(String contractAddress) {
        return TEST_CONTRACT_API_URL + "/" + contractAddress + "/token";
    }

    @Override
    public String tokenByTokenIdUrl(String contractAddress, String tokenId) {
        return TEST_CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId;
    }

    @Override
    public String tokenByTokenIdHistoryUrl(String contractAddress, String tokenId) {
        return TEST_CONTRACT_API_URL + "/" + contractAddress + "/token/" + tokenId + "/history";
    }

    @Override
    public String tokenByKlaytnAddressUrl(String contractAddress, String klaytnAddress) {
        return TEST_CONTRACT_API_URL + "/" + contractAddress + "/owner/" + klaytnAddress;
    }
}
