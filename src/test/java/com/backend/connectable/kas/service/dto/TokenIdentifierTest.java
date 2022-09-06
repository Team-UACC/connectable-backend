package com.backend.connectable.kas.service.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.backend.connectable.kas.service.token.dto.TokenIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenIdentifierTest {

    @DisplayName("hexadecimal 정보를 integer로 바꿔 가져올 수 있다.")
    @Test
    void hexToInt() {
        TokenIdentifier tokenIdentifier =
                new TokenIdentifier(
                        "0x1a",
                        "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/26.json");

        assertThat(tokenIdentifier.getTokenId()).isEqualTo(26);
    }
}
