package com.backend.connectable.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {

    @DisplayName("빌더 패턴을 사용하면, 유저를 만들 수 있다.")
    @Test
    void createUser() {
        // given
        Long id = 1L;
        String klaytnAddress = "0x1234";
        String nickname = "Joel";
        String phoneNumber = "010-1234-5678";
        boolean privacyAgreement = true;

        // when
        User joel = User.builder()
                .id(id)
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .build();

        // then
        assertThat(joel.getId()).isEqualTo(id);
        assertThat(joel.getKlaytnAddress()).isEqualTo(klaytnAddress);
        assertThat(joel.getNickname()).isEqualTo(nickname);
        assertThat(joel.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(joel.isPrivacyAgreement()).isEqualTo(privacyAgreement);
    }

    @DisplayName("닉네임을 유저가 가지고 있는지 검증할 수 있다.")
    @Test
    void hasNickname() {
        // given
        Long id = 1L;
        String klaytnAddress = "0x1234";
        String nickname = null;
        String phoneNumber = "010-1234-5678";
        boolean privacyAgreement = true;

        // when
        User noNickNameUser = User.builder()
                .id(id)
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .build();

        // then
        assertThat(noNickNameUser.hasNickname()).isFalse();
    }

    @DisplayName("유저가 핸드폰 번호를 가지고 있는지 검증할 수 있다.")
    @Test
    void hasPhoneNumber() {
        // given
        Long id = 1L;
        String klaytnAddress = "0x1234";
        String nickname = "nickname";
        String phoneNumber = null;
        boolean privacyAgreement = true;

        // when
        User noNickNameUser = User.builder()
                .id(id)
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .build();

        // then
        assertThat(noNickNameUser.hasPhoneNumber()).isFalse();
    }
}
