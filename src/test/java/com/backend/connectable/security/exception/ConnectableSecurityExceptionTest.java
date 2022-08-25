package com.backend.connectable.security.exception;

import com.backend.connectable.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ConnectableSecurityExceptionTest {

    @DisplayName("ErrorType을 에러메시지 JSON 형식으로 변환할 수 있다.")
    @Test
    void createException() {
        // given & when
        ConnectableSecurityException connectableSecurityException = new ConnectableSecurityException(ErrorType.INVALID_TOKEN);

        // then
        String expectedMessage =
            "{" +
                "\"errorCode\":\"" + ErrorType.INVALID_TOKEN.getErrorCode() + "\"," +
                "\"message\":\"" + ErrorType.INVALID_TOKEN.getMessage() + "\"" +
            "}";
        assertThat(connectableSecurityException.getMessage()).isEqualTo(expectedMessage);
    }
}