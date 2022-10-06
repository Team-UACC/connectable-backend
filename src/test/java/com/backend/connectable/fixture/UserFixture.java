package com.backend.connectable.fixture;

import com.backend.connectable.user.domain.User;

public class UserFixture {

    private UserFixture() {}

    public static User createUserMrLee() {
        return User.builder()
                .klaytnAddress("0x1111")
                .nickname("mrlee")
                .phoneNumber("000-0000-0000")
                .privacyAgreement(true)
                .isActive(true)
                .build();
    }

    public static User createUserJoel() {
        return User.builder()
                .klaytnAddress("0x1234")
                .nickname("조엘")
                .phoneNumber("010-8516-1399")
                .privacyAgreement(true)
                .isActive(true)
                .build();
    }
}
