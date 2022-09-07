package com.backend.connectable.kas.service.mockserver;

import static org.mockserver.model.HttpRequest.request;

import org.mockserver.model.HttpRequest;

/*
* Mocking Based On https://refs.klaytnapi.com/ko/kip17/latest#tag/kip17-token
* */
public class KasMockRequest {

    private KasMockRequest() {}

    public static String VALID_CONTRACT_ADDRESS = "0x1234";
    public static String VALID_CONTRACT_ADDRESS2 = "0x1111";
    public static String INVALID_CONTRACT_ADDRESS = "0x5678";

    public static String VALID_TOKEN_ID = "0x1";

    public static String INVALID_TOKEN_ID = "1";

    public static String VALID_ALIAS = "alias";

    public static String VALID_OWNER_ADDRESS = "0xabcd";
    public static String INVALID_OWNER_ADDRESS = "0xefgh";

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    public static HttpRequest GET_CONTRACTS = request().withMethod(GET).withPath("/contract");

    public static HttpRequest POST_CONTRACT = request().withMethod(POST).withPath("/contract");

    public static HttpRequest GET_CONTRACT_BY_ADDRESS =
            request().withMethod(GET).withPath("/contract/" + VALID_CONTRACT_ADDRESS);

    public static HttpRequest GET_CONTRACT_BY_ALIAS =
            request().withMethod(GET).withPath("/contract/" + VALID_ALIAS);

    public static HttpRequest GET_CONTRACT_BY_INVALID_ADDRESS =
            request().withMethod(GET).withPath("/contract/" + INVALID_CONTRACT_ADDRESS);

    public static HttpRequest POST_TOKEN =
            request().withMethod(POST).withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token");

    public static HttpRequest GET_TOKENS =
            request().withMethod(GET).withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token");

    public static HttpRequest GET_TOKEN_BY_TOKEN_ID =
            request()
                    .withMethod(GET)
                    .withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token/" + VALID_TOKEN_ID);

    public static HttpRequest GET_TOKEN_BY_INVALID_TOKEN_ID =
            request()
                    .withMethod(GET)
                    .withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token/" + INVALID_TOKEN_ID);

    public static HttpRequest POST_TOKEN_SEND =
            request()
                    .withMethod(POST)
                    .withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token/" + VALID_TOKEN_ID);

    public static HttpRequest POST_TOKEN_SEND_BY_INVALID_TOKEN_ID =
            request()
                    .withMethod(POST)
                    .withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token/" + INVALID_TOKEN_ID);

    public static HttpRequest DELETE_TOKEN =
            request()
                    .withMethod(DELETE)
                    .withPath("/contract/" + VALID_CONTRACT_ADDRESS + "/token/" + VALID_TOKEN_ID);

    public static HttpRequest GET_TOKENS_OF_USER =
            request()
                    .withMethod(GET)
                    .withPath(
                            "/contract/"
                                    + VALID_CONTRACT_ADDRESS
                                    + "/owner/"
                                    + VALID_OWNER_ADDRESS);

    public static HttpRequest GET_TOKENS_OF_USER2 =
            request()
                    .withMethod(GET)
                    .withPath(
                            "/contract/"
                                    + VALID_CONTRACT_ADDRESS2
                                    + "/owner/"
                                    + VALID_OWNER_ADDRESS);

    public static HttpRequest GET_TOKENS_OF_USER_BY_INVALID_OWNER_ADDRESS =
            request()
                    .withMethod(GET)
                    .withPath(
                            "/contract/"
                                    + VALID_CONTRACT_ADDRESS
                                    + "/owner/"
                                    + INVALID_OWNER_ADDRESS);

    public static HttpRequest GET_TOKEN_HISTORY =
            request()
                    .withMethod(GET)
                    .withPath(
                            "/contract/"
                                    + VALID_CONTRACT_ADDRESS
                                    + "/token/"
                                    + VALID_TOKEN_ID
                                    + "/history");

    public static HttpRequest GET_TOKEN_HISTORY_BY_INVALID_TOKEN_ID =
            request()
                    .withMethod(GET)
                    .withPath(
                            "/contract/"
                                    + VALID_CONTRACT_ADDRESS
                                    + "/token/"
                                    + INVALID_TOKEN_ID
                                    + "/history");
}
