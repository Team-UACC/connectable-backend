package com.backend.connectable.kas.service.mockserver;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static org.mockserver.model.HttpResponse.response;

import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

/*
 * Mocking Based On https://refs.klaytnapi.com/ko/kip17/latest#tag/kip17-token
 * */
public class KasMockResponse {

    private KasMockResponse() {}

    public static HttpResponse GET_CONTRACTS =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"cursor\": \"eyJjm...ZSJ9\",\n"
                                    + "  \"items\": [\n"
                                    + "    {\n"
                                    + "      \"address\": \"0x34132f491fea7aa622cca594e4bd2ccd6fc02bf7\",\n"
                                    + "      \"alias\": \"test\",\n"
                                    + "      \"name\": \"TEST NFT\",\n"
                                    + "      \"symbol\": \"TEST\",\n"
                                    + "      \"options\": {\n"
                                    + "        \"enableGlobalFeePayer\": true,\n"
                                    + "        \"userFeePayer\": {\n"
                                    + "          \"krn\": \"krn:1001:wallet:88c1223c-66af-4122-9818-069b2e3c6b30:feepayer-pool:default\",\n"
                                    + "          \"address\": \"0xE8964cA0C83cBbF520df5597dc1f5EFc27E5E729\"\n"
                                    + "        }\n"
                                    + "      }\n"
                                    + "    }\n"
                                    + "  ]\n"
                                    + "}");

    public static HttpResponse GET_CONTRACT =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"address\": \"0x7766a3af39e4fffbaccf6efa6a37ed195f9179b4\",\n"
                                    + "  \"alias\": \"test\",\n"
                                    + "  \"name\": \"Test NFT\",\n"
                                    + "  \"symbol\": \"TEST\",\n"
                                    + "  \"options\": {\n"
                                    + "    \"enableGlobalFeePayer\": true,\n"
                                    + "    \"userFeePayer\": {\n"
                                    + "      \"krn\": \"krn:1001:wallet:88c1223c-66af-4122-9818-069b2e3c6b30:feepayer-pool:default\",\n"
                                    + "      \"address\": \"0xE8964cA0C83cBbF520df5597dc1f5EFc27E5E729\"\n"
                                    + "    }\n"
                                    + "  }\n"
                                    + "}");

    public static HttpResponse POST_CONTRACT =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"status\": \"Submitted\",\n"
                                    + "  \"transactionHash\": \"0x0a4f4f97d8a5904721514d60abd3f9ad7938862e415a6c043553a94ab68a5edb\",\n"
                                    + "  \"owner\": \"0xa809284C83b901eD106Aba4Ccda14628Af128e14\",\n"
                                    + "  \"options\": {\n"
                                    + "    \"enableGlobalFeePayer\": true,\n"
                                    + "    \"userFeePayer\": {\n"
                                    + "      \"krn\": \"krn:1001:wallet:88c1223c-66af-4122-9818-069b2e3c6b30:feepayer-pool:default\",\n"
                                    + "      \"address\": \"0xE8964cA0C83cBbF520df5597dc1f5EFc27E5E729\"\n"
                                    + "    }\n"
                                    + "  }\n"
                                    + "}");

    public static HttpResponse POST_TOKEN =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"status\": \"Submitted\",\n"
                                    + "  \"transactionHash\": \"0x57d94989611cf0351fdea04dd5193398980642aaed82c8c34bdef12f4ecf356e\"\n"
                                    + "}");

    public static HttpResponse GET_TOKENS =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"cursor\": \"eyJjm...ZSJ9\",\n"
                                    + "  \"items\": [\n"
                                    + "    {\n"
                                    + "      \"createdAt\": 1608640582,\n"
                                    + "      \"updatedAt\": 1608640582,\n"
                                    + "      \"owner\": \"0xdc277e2d89b92336a4ee80be3c7142443fdade47\",\n"
                                    + "      \"previousOwner\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"tokenId\": \"0x9\",\n"
                                    + "      \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "      \"transactionHash\": \"0xb5fcf5f9bb28e6584104b743630371cef234c176a5c42b65e5a01299c0c2e6ff\"\n"
                                    + "    },\n"
                                    + "    {\n"
                                    + "      \"createdAt\": 1607396973,\n"
                                    + "      \"updatedAt\": 1607396973,\n"
                                    + "      \"owner\": \"0xdc277e2d89b92336a4ee80be3c7142443fdade47\",\n"
                                    + "      \"previousOwner\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"tokenId\": \"0x8\",\n"
                                    + "      \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "      \"transactionHash\": \"0xd2b5d09aa13e6306ef65507c324c92d3f91aa03003ed480fe8a11e4adca713d4\"\n"
                                    + "    }\n"
                                    + "  ]\n"
                                    + "}");

    public static HttpResponse GET_TOKEN =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"createdAt\": 1607330287,\n"
                                    + "  \"owner\": \"0xae7bb7efff289c163a95303795c4d59293f6ba92\",\n"
                                    + "  \"previousOwner\": \"0xbafa182d3fccebcb1701b66a69f0e7b40f3a52c4\",\n"
                                    + "  \"tokenId\": \"0x1\",\n"
                                    + "  \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "  \"transactionHash\": \"0xf5f5d97ce5e6339fc767bd2f4df57d9ab39ca71e423d9f3b36a5f58ac894f578\",\n"
                                    + "  \"updatedAt\": 1607391306\n"
                                    + "}");

    public static HttpResponse POST_TOKEN_SEND =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"status\": \"Submitted\",\n"
                                    + "  \"transactionHash\": \"0x72b05d3b27e17a132e60681f4a2ad487868181b0623fbaa841875ecb36f62b8f\"\n"
                                    + "}");

    public static HttpResponse DELETE_TOKEN =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"status\": \"Submitted\",\n"
                                    + "  \"transactionHash\": \"0xbf6de339cf435447e58da05384bbf83a16fea40044bb49b84e11d3b00ea0325f\"\n"
                                    + "}");

    public static HttpResponse GET_TOKENS_OF_USER =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"cursor\": \"eyJjm...ZSJ9\",\n"
                                    + "  \"items\": [\n"
                                    + "    {\n"
                                    + "      \"createdAt\": 1608691873,\n"
                                    + "      \"updatedAt\": 1608691873,\n"
                                    + "      \"owner\": \"0x9eaf20b40e0f1ced5dbba6f5cfb0d3e12b0534f4\",\n"
                                    + "      \"previousOwner\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"tokenId\": \"0x13\",\n"
                                    + "      \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "      \"transactionHash\": \"0x3bf8107f39158abf0f2b0dc1ee8ccce9320fbce14899f0021dc4d6ad970c9150\"\n"
                                    + "    },\n"
                                    + "    {\n"
                                    + "      \"createdAt\": 1608690798,\n"
                                    + "      \"updatedAt\": 1608690798,\n"
                                    + "      \"owner\": \"0x9eaf20b40e0f1ced5dbba6f5cfb0d3e12b0534f4\",\n"
                                    + "      \"previousOwner\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"tokenId\": \"0x12\",\n"
                                    + "      \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "      \"transactionHash\": \"0x3857bd481b4a993b4346e1b6c18862bb39ee19401fd3b04101993ce8eed05743\"\n"
                                    + "    }\n"
                                    + "  ]\n"
                                    + "}");

    public static HttpResponse GET_TOKENS_OF_USER2 =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"cursor\": \"eyJjm...ZSJ9\",\n"
                                    + "  \"items\": [\n"
                                    + "    {\n"
                                    + "      \"createdAt\": 1608691873,\n"
                                    + "      \"updatedAt\": 1608691873,\n"
                                    + "      \"owner\": \"0x9eaf20b40e0f1ced5dbba6f5cfb0d3e12b0534f4\",\n"
                                    + "      \"previousOwner\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"tokenId\": \"0x13\",\n"
                                    + "      \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "      \"transactionHash\": \"0x3bf8107f39158abf0f2b0dc1ee8ccce9320fbce14899f0021dc4d6ad970c9150\"\n"
                                    + "    },\n"
                                    + "    {\n"
                                    + "      \"createdAt\": 1608690798,\n"
                                    + "      \"updatedAt\": 1608690798,\n"
                                    + "      \"owner\": \"0x9eaf20b40e0f1ced5dbba6f5cfb0d3e12b0534f4\",\n"
                                    + "      \"previousOwner\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"tokenId\": \"0x12\",\n"
                                    + "      \"tokenUri\": \"https://metastore.kip17.com/0xbe02aba/0x1\",\n"
                                    + "      \"transactionHash\": \"0x3857bd481b4a993b4346e1b6c18862bb39ee19401fd3b04101993ce8eed05743\"\n"
                                    + "    }\n"
                                    + "  ]\n"
                                    + "}");

    public static HttpResponse GET_TOKEN_HISTORY =
            response()
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"cursor\": \"eyJjm...ZSJ9\",\n"
                                    + "  \"items\": [\n"
                                    + "    {\n"
                                    + "      \"from\": \"0xbafa182d3fccebcb1701b66a69f0e7b40f3a52c4\",\n"
                                    + "      \"timestamp\": 1607391306,\n"
                                    + "      \"to\": \"0xae7bb7efff289c163a95303795c4d59293f6ba92\"\n"
                                    + "    },\n"
                                    + "    {\n"
                                    + "      \"from\": \"0x0000000000000000000000000000000000000000\",\n"
                                    + "      \"timestamp\": 1607330287,\n"
                                    + "      \"to\": \"0xbafa182d3fccebcb1701b66a69f0e7b40f3a52c4\"\n"
                                    + "    }\n"
                                    + "  ]\n"
                                    + "}");

    public static HttpResponse BAD_REQUEST_RESPONSE =
            response()
                    .withStatusCode(BAD_REQUEST.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"code\": 1100050,\n"
                                    + "  \"message\": \"Invalid request\"\n"
                                    + "}");

    public static HttpResponse UNAUTHORIZED_RESPONSE =
            response()
                    .withStatusCode(UNAUTHORIZED.code())
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(
                            "{\n"
                                    + "  \"code\": 1010009,\n"
                                    + "  \"message\": \"The credential you provided is not valid.\",\n"
                                    + "  \"requestId\": \"1b370cb5-590d-90c2-bf4f-afb89c0b6a5a\"\n"
                                    + "}");
}
