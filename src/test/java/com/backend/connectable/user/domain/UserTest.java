package com.backend.connectable.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @DisplayName("빌더 패턴을 사용하면, 유저를 만들 수 있다.")
    @Test
    void createUser() {
        // given
        Long id = 1L;
        String walletAddress = "0x1234";
        String nickname = "Joel";
        String phoneNumber = "010-1234-5678";
        boolean privacyAgreement = true;

        // when
        User joel = User.builder()
                .id(id)
                .walletAddress(walletAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .build();

        // then
        assertThat(joel.getId()).isEqualTo(id);
        assertThat(joel.getWalletAddress()).isEqualTo(walletAddress);
        assertThat(joel.getNickname()).isEqualTo(nickname);
        assertThat(joel.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(joel.isPrivacyAgreement()).isEqualTo(privacyAgreement);
    }
}
