package com.backend.connectable.global.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OpenseaCollectionNamingUtilTest {

    @DisplayName("Opensea Collection Url로 해당 NFT Name을 변경할 수 있다.")
    @Test
    void toOpenseaCollectionUrl() {
        // given
        String contractName = "Connectable Dev NFT";

        // when
        String openseaUrl = OpenseaCollectionNamingUtil.toOpenseaCollectionUrl(contractName);

        // then
        assertThat(openseaUrl).isEqualTo("https://opensea.io/collection/connectable-dev-nft");
    }
}