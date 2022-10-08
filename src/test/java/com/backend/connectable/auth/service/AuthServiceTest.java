package com.backend.connectable.auth.service;

import com.backend.connectable.exception.ConnectableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {

    @Autowired private AuthService authService;

    private final String phoneNumber = "010-2222-3333";
    private final Long duration = 1L;

    @DisplayName("인증키 획득 요청시 문자열 형태의 인증키를 반환받는다.")
    @Test
    void getAuthKey() {
        // given & when
        String authKey = authService.getAuthKey(phoneNumber, duration);

        // then
        assertThat(authKey).isNotBlank();
    }

    @DisplayName("발급받은 인증키로 인증 시 검증에 성공한다.")
    @Test
    void certifyKey() {
        // give & when
        String generatedKey = authService.getAuthKey(phoneNumber, duration);
        Boolean isCertified = authService.certifyKey(phoneNumber, generatedKey);

        // then
        assertThat(isCertified).isTrue();
    }

    @DisplayName("서로 다른 요청에 의해 발급된 인증키는 같지 않아야 한다.")
    @Test
    void getAuthKeyDifference() {
        // given & when
        String generatedKey1 = authService.getAuthKey(phoneNumber, duration);
        String generatedKey2 = authService.getAuthKey(phoneNumber, duration);

        // then
        assertThat(generatedKey1).isNotEqualTo(generatedKey2);
    }

    @DisplayName("인증에 실패시 false값을 예외처리에 의해 ConnectableException을 발생시킨다.")
    @Test
    void validateAuthKeyFail() {
        // given & when
        String generatedKey = authService.getAuthKey(phoneNumber, duration);
        String authKey = "a1b2c3";

        // then
        assertThatThrownBy(() -> authService.certifyKey(generatedKey, authKey))
                .isInstanceOf(ConnectableException.class);
    }
}
