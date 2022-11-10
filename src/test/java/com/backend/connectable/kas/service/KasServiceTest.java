package com.backend.connectable.kas.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.backend.connectable.exception.KasException;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.contract.dto.ContractDeployResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemsResponse;
import com.backend.connectable.kas.service.mockserver.KasMockRequest;
import com.backend.connectable.kas.service.mockserver.KasServiceMockSetup;
import com.backend.connectable.kas.service.token.dto.TokenHistoriesResponse;
import com.backend.connectable.kas.service.token.dto.TokenIdentifier;
import com.backend.connectable.kas.service.token.dto.TokenResponse;
import com.backend.connectable.kas.service.token.dto.TokensResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KasServiceTest extends KasServiceMockSetup {

    @DisplayName("KAS에서 배포한 컨트랙트를 확인할 수 있다.")
    @Test
    void getMyContracts() {
        // given & when
        ContractItemsResponse response = kasService.getMyContracts();

        // then
        assertThat(response.getCursor()).isNotEmpty();
        assertThat(response.getItems()).isNotNull();
    }

    @DisplayName("KAS에서 배포한 컨트랙트의 주소를 입력하면, 컨트랙트 정보를 받을 수 있다.")
    @Test
    void getMyContract() {
        // given & when
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        ContractItemResponse response = kasService.getMyContract(contractAddress);

        // then
        assertThat(response.getAddress()).isNotEmpty();
        assertThat(response.getAlias()).isNotEmpty();
        assertThat(response.getName()).isNotEmpty();
        assertThat(response.getSymbol()).isNotEmpty();
    }

    @DisplayName("KAS에서 배포한 컨트랙트의 alias를 입력하면, 컨트랙트 정보를 받을 수 있다.")
    @Test
    void getMyContractByAlias() {
        // given & when
        String alias = KasMockRequest.VALID_ALIAS;
        ContractItemResponse response = kasService.getMyContractByAlias(alias);

        // then
        assertThat(response.getAddress()).isNotEmpty();
        assertThat(response.getAlias()).isNotEmpty();
        assertThat(response.getName()).isNotEmpty();
        assertThat(response.getSymbol()).isNotEmpty();
    }

    @DisplayName("KAS에서 배포한 컨트랙트가 아니라면, KAS Exception이 발생한다.")
    @Test
    void getMyContractException() {
        // given
        String contractAddress = KasMockRequest.INVALID_CONTRACT_ADDRESS;

        // when & then
        assertThatThrownBy(() -> kasService.getMyContract(contractAddress))
                .isInstanceOf(KasException.class);
    }

    @DisplayName("KAS에서 컨트랙트를 배포할 수 있다.")
    @Test
    void deployContract() {
        // given & when
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;
        ContractDeployResponse response =
                kasService.deployContract("name", "symbol", "alias", owner);

        // then
        assertThat(response.getOwner()).isNotEmpty();
        assertThat(response.getStatus()).isNotEmpty();
        assertThat(response.getTransactionHash()).isNotEmpty();
    }

    @DisplayName("KAS에서 자신의 게정 주소로 컨트랙트를 배포할 수 있다.")
    @Test
    void deployMyContract() {
        // given & when
        ContractDeployResponse response = kasService.deployMyContract("name", "symbol", "alias");

        // then
        assertThat(response.getOwner()).isNotEmpty();
        assertThat(response.getStatus()).isNotEmpty();
        assertThat(response.getTransactionHash()).isNotEmpty();
    }

    @DisplayName("KAS를 통해 배포한 컨트랙트의 토큰을 민팅할 수 있다.")
    @Test
    void mintToken() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String tokenId = "0x1";
        String tokenUri = "https://token.uri";
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;

        // when
        TransactionResponse response =
                kasService.mintToken(contractAddress, tokenId, tokenUri, owner);

        // then
        assertThat(response.getStatus()).isNotEmpty();
        assertThat(response.getTransactionHash()).isNotEmpty();
    }

    @DisplayName("KAS를 통해 배포한 컨트랙트의 토큰을 본인의 주소로 민팅할 수 있다.")
    @Test
    void mintMyToken() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String tokenId = "0x1";
        int tokenIdByInt = 1;
        String tokenUri = "https://token.uri";

        // when
        TransactionResponse response1 = kasService.mintMyToken(contractAddress, tokenId, tokenUri);
        TransactionResponse response2 =
                kasService.mintMyToken(contractAddress, tokenIdByInt, tokenUri);

        // then
        assertThat(response1.getStatus()).isNotEmpty();
        assertThat(response1.getTransactionHash()).isNotEmpty();

        assertThat(response2.getStatus()).isNotEmpty();
        assertThat(response2.getTransactionHash()).isNotEmpty();
    }

    @DisplayName("KAS를 통해 해당 컨트랙트에 민팅된 토큰을 전체 조회할 수 있다.")
    @Test
    void getTokens() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;

        // when
        TokensResponse response = kasService.getTokens(contractAddress);

        // then
        assertThat(response.getItems()).isNotNull();
        assertThat(response.getTokenUris()).isNotNull();
    }

    @DisplayName("KAS를 통해 민팅된 토큰을 조회할 수 있다.")
    @Test
    void getToken() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String tokenId = KasMockRequest.VALID_TOKEN_ID;
        int tokenIdByInt = KasMockRequest.VALID_TOKEN_ID_BY_INT;

        // when
        TokenResponse response1 = kasService.getToken(contractAddress, tokenId);
        TokenResponse response2 = kasService.getToken(contractAddress, tokenIdByInt);

        // then
        assertThat(response1.getOwner()).isNotEmpty();
        assertThat(response1.getTokenId()).isNotEmpty();
        assertThat(response1.getTransactionHash()).isNotEmpty();

        assertThat(response2.getOwner()).isNotEmpty();
        assertThat(response2.getTokenId()).isNotEmpty();
        assertThat(response2.getTransactionHash()).isNotEmpty();
    }

    @DisplayName("KAS를 통해 민팅되지 않은 토큰은 조회시 KasException이 발생한다.")
    @Test
    void getTokenKasException() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String invalidTokenId = KasMockRequest.INVALID_TOKEN_ID;

        // when
        assertThatThrownBy(() -> kasService.getToken(contractAddress, invalidTokenId))
                .isInstanceOf(KasException.class);
    }

    @DisplayName("KAS를 통해 민팅된 토큰을 전송할 수 있다.")
    @Test
    void sendMyToken() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String tokenId = KasMockRequest.VALID_TOKEN_ID;
        int tokenIdByInt = KasMockRequest.VALID_TOKEN_ID_BY_INT;
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;

        // when
        TransactionResponse response1 = kasService.sendMyToken(contractAddress, tokenId, owner);
        TransactionResponse response2 =
                kasService.sendMyToken(contractAddress, tokenIdByInt, owner);

        // then
        assertThat(response1.getTransactionHash()).isNotEmpty();
        assertThat(response1.getStatus()).isNotEmpty();

        assertThat(response2.getTransactionHash()).isNotEmpty();
        assertThat(response2.getStatus()).isNotEmpty();
    }

    @DisplayName("민팅되지 않은 토큰을 전송시, KasException이 발생한다.")
    @Test
    void sendMyTokenException() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String invalidTokenId = KasMockRequest.INVALID_TOKEN_ID;
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;

        // when & then
        assertThatThrownBy(() -> kasService.sendMyToken(contractAddress, invalidTokenId, owner))
                .isInstanceOf(KasException.class);
    }

    @DisplayName("발행한 토큰을 소각할 수 있다")
    @Test
    void burnMyToken() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String tokenId = KasMockRequest.VALID_TOKEN_ID;
        int tokenIdByInt = KasMockRequest.VALID_TOKEN_ID_BY_INT;

        // when
        TransactionResponse response1 = kasService.burnMyToken(contractAddress, tokenId);
        TransactionResponse response2 = kasService.burnMyToken(contractAddress, tokenIdByInt);

        // then
        assertThat(response1.getStatus()).isNotEmpty();
        assertThat(response1.getTransactionHash()).isNotEmpty();

        assertThat(response2.getStatus()).isNotEmpty();
        assertThat(response2.getTransactionHash()).isNotEmpty();
    }

    @DisplayName("토큰의 소유자 변경 이력을 조회할 수 있다.")
    @Test
    void getTokenHistory() {
        // given
        String contractAddress = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String tokenId = KasMockRequest.VALID_TOKEN_ID;
        int tokenIdByInt = KasMockRequest.VALID_TOKEN_ID_BY_INT;

        // when
        TokenHistoriesResponse response1 = kasService.getTokenHistory(contractAddress, tokenId);
        TokenHistoriesResponse response2 =
                kasService.getTokenHistory(contractAddress, tokenIdByInt);

        // then
        assertThat(response1.getCursor()).isNotEmpty();
        assertThat(response1.getItems()).isNotNull();

        assertThat(response2.getCursor()).isNotEmpty();
        assertThat(response2.getItems()).isNotNull();
    }

    @DisplayName("사용자가 소유한 모든 토큰을 조회할 수 있다.")
    @Test
    void findAllTokensOwnedByUser() {
        // given
        String contractAddress1 = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String contractAddress2 = KasMockRequest.VALID_CONTRACT_ADDRESS2;
        List<String> contractAddresses = Arrays.asList(contractAddress1, contractAddress2);
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;

        // when
        List<TokenIdentifier> response =
                kasService.findAllTokensOwnedByUser(contractAddresses, owner);

        // then
        assertThat(response).isNotEmpty();
    }

    @DisplayName("컨트랙트 주소들과 유저의 클레이튼 주소로 홀더인지 검증할 수 있다.")
    @Test
    void checkIsTokenHolder() {
        // given
        String contractAddress1 = KasMockRequest.VALID_CONTRACT_ADDRESS;
        String contractAddress2 = KasMockRequest.VALID_CONTRACT_ADDRESS2;
        List<String> contractAddresses = Arrays.asList(contractAddress1, contractAddress2);
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;

        // when
        boolean isHolder = kasService.checkIsTokenHolder(contractAddresses, owner);

        // then
        assertThat(isHolder).isTrue();
    }

    @DisplayName("컨트랙트 주소들과 유저의 클레이튼 주소로 홀더가 아닌지 검증할 수 있다.")
    @Test
    void checkIsNotTokenHolder() {
        // given
        String contractAddress1 = KasMockRequest.NO_HOLDER_CONTRACT_ADDRESS;
        String contractAddress2 = KasMockRequest.NO_HOLDER_CONTRACT_ADDRESS2;
        List<String> contractAddresses = Arrays.asList(contractAddress1, contractAddress2);
        String owner = KasMockRequest.VALID_OWNER_ADDRESS;

        // when
        boolean isHolder = kasService.checkIsTokenHolder(contractAddresses, owner);

        // then
        assertThat(isHolder).isFalse();
    }
}
