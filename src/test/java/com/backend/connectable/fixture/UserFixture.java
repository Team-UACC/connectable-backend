package com.backend.connectable.fixture;

import static com.backend.connectable.kas.service.mockserver.KasMockRequest.VALID_OWNER_ADDRESS;

import com.backend.connectable.user.domain.User;

public class UserFixture {

    private UserFixture() {}

    public static User createUserMrLee() {
        return User.builder()
                .klaytnAddress(VALID_OWNER_ADDRESS)
                .nickname("mrlee")
                .phoneNumber("000-0000-0000")
                .privacyAgreement(true)
                .isActive(true)
                .build();
    }

    public static User createUserJoel() {
        return User.builder()
                .klaytnAddress(VALID_OWNER_ADDRESS)
                .nickname("조엘")
                .phoneNumber("010-8516-1399")
                .privacyAgreement(true)
                .isActive(true)
                .build();
    }
}
