package com.backend.connectable.kas.service.mockserver;

import com.backend.connectable.kas.service.KasService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({KasMockConfig.class})
public class KasServiceMockSetup {

    public static final int MOCK_SERVER_PORT = 8888;
    public static final String MOCK_SERVER_IP = "localhost";

    private ClientAndServer mockServer;

    @Autowired protected KasService kasService;

    @BeforeEach
    void setUpMockServer() {
        mockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
        // Contract
        setUpMockServerApi(KasMockRequest.GET_CONTRACTS, KasMockResponse.GET_CONTRACTS);
        setUpMockServerApi(KasMockRequest.POST_CONTRACT, KasMockResponse.POST_CONTRACT);
        setUpMockServerApi(KasMockRequest.GET_CONTRACT_BY_ADDRESS, KasMockResponse.GET_CONTRACT);
        setUpMockServerApi(KasMockRequest.GET_CONTRACT_BY_ALIAS, KasMockResponse.GET_CONTRACT);
        setUpMockServerApi(
                KasMockRequest.GET_CONTRACT_BY_INVALID_ADDRESS,
                KasMockResponse.BAD_REQUEST_RESPONSE);

        // Token
        setUpMockServerApi(KasMockRequest.POST_TOKEN, KasMockResponse.POST_TOKEN);
        setUpMockServerApi(KasMockRequest.GET_TOKENS, KasMockResponse.GET_TOKENS);
        setUpMockServerApi(KasMockRequest.GET_TOKEN_BY_TOKEN_ID, KasMockResponse.GET_TOKEN);
        setUpMockServerApi(
                KasMockRequest.GET_TOKEN_BY_INVALID_TOKEN_ID, KasMockResponse.BAD_REQUEST_RESPONSE);
        setUpMockServerApi(KasMockRequest.POST_TOKEN_SEND, KasMockResponse.POST_TOKEN_SEND);
        setUpMockServerApi(
                KasMockRequest.POST_TOKEN_SEND_BY_INVALID_TOKEN_ID,
                KasMockResponse.BAD_REQUEST_RESPONSE);
        setUpMockServerApi(KasMockRequest.DELETE_TOKEN, KasMockResponse.DELETE_TOKEN);
        setUpMockServerApi(KasMockRequest.GET_TOKENS_OF_USER, KasMockResponse.GET_TOKENS_OF_USER);
        setUpMockServerApi(
                KasMockRequest.GET_TOKENS_OF_USER_BY_INVALID_OWNER_ADDRESS,
                KasMockResponse.BAD_REQUEST_RESPONSE);
        setUpMockServerApi(KasMockRequest.GET_TOKENS_OF_USER, KasMockResponse.GET_TOKENS_OF_USER);
        setUpMockServerApi(KasMockRequest.GET_TOKENS_OF_USER2, KasMockResponse.GET_TOKENS_OF_USER2);
        setUpMockServerApi(
                KasMockRequest.GET_TOKENS_OF_USER_BY_INVALID_OWNER_ADDRESS,
                KasMockResponse.BAD_REQUEST_RESPONSE);
        setUpMockServerApi(KasMockRequest.GET_TOKEN_HISTORY, KasMockResponse.GET_TOKEN_HISTORY);
        setUpMockServerApi(
                KasMockRequest.GET_TOKEN_HISTORY_BY_INVALID_TOKEN_ID,
                KasMockResponse.BAD_REQUEST_RESPONSE);
    }

    void setUpMockServerApi(HttpRequest httpRequest, HttpResponse httpResponse) {
        new MockServerClient(MOCK_SERVER_IP, MOCK_SERVER_PORT)
                .when(httpRequest)
                .respond(httpResponse);
    }

    @AfterEach
    void shutDown() {
        mockServer.stop();
    }
}
