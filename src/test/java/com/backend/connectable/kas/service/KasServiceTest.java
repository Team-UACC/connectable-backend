package com.backend.connectable.kas.service;

import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.contract.dto.ContractDeployResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemsResponse;
import com.backend.connectable.kas.service.token.dto.TokenResponse;
import com.backend.connectable.kas.service.token.dto.TokensResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled
class KasServiceTest {

    @Autowired KasService kasService;

    private static final String JOEL_KAIKAS = "0xBc29741452272c432e8CD3984b4c7f2362dFf7f0";
    private static final String TOKEN_URI =
            "https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json";

    private static String NEW_CONTRACT_ADDRESS = "0xd933e90917791e8441f42478ea1e882f599b1941";
    private static String NEW_CONTRACT_NAME = "";

    @Value("${kas.settings.account-pool-address}")
    public String poolAddress;

    @Test
    void getContractInfo() {
        ContractItemResponse myContract = kasService.getMyContract(NEW_CONTRACT_ADDRESS);
        System.out.println("myContract.getAlias() = " + myContract.getAlias());
        System.out.println("myContract.getName() = " + myContract.getName());
    }

    @Test
    void findAllTokensOfContractAddressesOwnedByUser() throws InterruptedException {
        ContractItemsResponse myContracts = kasService.getMyContracts();
        List<String> myContractAddresses =
                myContracts.getItems().stream()
                        .map(ContractItemResponse::getAddress)
                        .filter(address -> !address.equalsIgnoreCase("updateme"))
                        .collect(Collectors.toList());
        System.out.println("MyContract: " + myContractAddresses);

        for (String myContractAddress : myContractAddresses) {
            int randomId = (int) Math.floor(Math.random() * (1000 - 100 + 1) + 100);
            kasService.mintMyToken(
                    myContractAddress,
                    randomId,
                    "https://" + myContractAddress + "/" + randomId + ".json");
        }

        Thread.sleep(5000);

        Map<String, TokensResponse> allTokensOfContractAddressesOwnedByUser =
                kasService.findAllTokensOfContractAddressesOwnedByUser(
                        myContractAddresses, poolAddress);
        for (TokensResponse value : allTokensOfContractAddressesOwnedByUser.values()) {
            System.out.println(value.getTokenUris());
        }
    }

    @Test
    @Order(1)
    void deploy() throws InterruptedException {
        String name = RandomStringUtils.randomAlphabetic(7);
        String symbol = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String alias = symbol.toLowerCase();
        NEW_CONTRACT_NAME = name;
        System.out.println(name + "--" + symbol + "--" + alias + "=>" + NEW_CONTRACT_NAME);

        ContractDeployResponse contractDeployResponse =
                kasService.deployMyContract(name, symbol, alias);
        System.out.println(contractDeployResponse.getOwner());
        System.out.println(contractDeployResponse.getStatus());
        System.out.println(contractDeployResponse.getTransactionHash());

        Thread.sleep(5000);
    }

    @Test
    @Order(2)
    void getMyContracts() throws InterruptedException {
        ContractItemsResponse myContracts = kasService.getMyContracts();
        System.out.println(myContracts.getCursor());
        List<ContractItemResponse> items = myContracts.getItems();
        for (ContractItemResponse item : items) {
            if (item.getName().equalsIgnoreCase(NEW_CONTRACT_NAME)) {
                if (!item.getAddress().equalsIgnoreCase("updateme")) {
                    NEW_CONTRACT_ADDRESS = item.getAddress();
                    System.out.println("새로 배포된 컨트랙트 주소: " + NEW_CONTRACT_ADDRESS);
                }
            }
            System.out.print(item.getName() + "++");
            System.out.print(item.getAddress() + "++");
            System.out.print(item.getAlias() + "++");
            System.out.print(item.getSymbol() + "++");
            System.out.println(item.getChainId() + "++");
        }

        Thread.sleep(2000);
    }

    @Test
    @Order(3)
    void getMyContract() throws InterruptedException {
        System.out.println("newlyDeployedContractAddress = " + NEW_CONTRACT_ADDRESS);
        ContractItemResponse item = kasService.getMyContract(NEW_CONTRACT_ADDRESS);
        System.out.print(item.getAddress() + "++");
        System.out.print(item.getAlias() + "++");
        System.out.print(item.getName() + "++");
        System.out.print(item.getSymbol() + "++");
        System.out.println(item.getChainId() + "++");

        Thread.sleep(2000);
    }

    @Test
    @Order(4)
    void mintToken() throws InterruptedException {
        TransactionResponse transactionResponse =
                kasService.mintToken(NEW_CONTRACT_ADDRESS, "0x1", TOKEN_URI, JOEL_KAIKAS);
        System.out.println(transactionResponse.getStatus());

        Thread.sleep(2000);
    }

    @Test
    @Order(5)
    void mintMyToken() throws InterruptedException {
        TransactionResponse transactionResponse =
                kasService.mintMyToken(NEW_CONTRACT_ADDRESS, "0x2", TOKEN_URI);
        System.out.println(transactionResponse.getStatus());

        Thread.sleep(2000);
    }

    @Test
    @Order(6)
    void getTokens() throws InterruptedException {
        TokensResponse tokens = kasService.getTokens(NEW_CONTRACT_ADDRESS);
        System.out.println("tokens.getCursor() = " + tokens.getCursor());
        List<TokenResponse> items = tokens.getItems();
        for (TokenResponse item : items) {
            System.out.println("item.getOwner() = " + item.getOwner());
            System.out.println("item.getPreviousOwner() = " + item.getPreviousOwner());
            System.out.println("item.getTokenId() = " + item.getTokenId());
            System.out.println("item.getTokenUri() = " + item.getTokenUri());
            System.out.println("item.getTransactionHash() = " + item.getTransactionHash());
        }

        Thread.sleep(2000);
    }

    @Test
    @Order(7)
    void getToken() throws InterruptedException {
        TokenResponse token = kasService.getToken(NEW_CONTRACT_ADDRESS, "0x1");
        System.out.println("token.getOwner() = " + token.getOwner());
        System.out.println("token.getPreviousOwner() = " + token.getPreviousOwner());
        System.out.println("token.getTokenId() = " + token.getTokenId());
        System.out.println("token.getTokenUri() = " + token.getTokenUri());
        System.out.println("token.getTransactionHash() = " + token.getTransactionHash());

        Thread.sleep(2000);
    }

    @Test
    @Order(8)
    void sendToken() throws InterruptedException {
        TransactionResponse transactionResponse =
                kasService.sendMyToken(NEW_CONTRACT_ADDRESS, "0x2", JOEL_KAIKAS);
        System.out.println("transactionResponse.getStatus() = " + transactionResponse.getStatus());
        System.out.println(
                "transactionResponse.getTransactionHash() = "
                        + transactionResponse.getTransactionHash());

        Thread.sleep(2000);
    }
}
