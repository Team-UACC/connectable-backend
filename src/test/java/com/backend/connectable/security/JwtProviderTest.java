package com.backend.connectable.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("JwtProvider를 이용하여, jwt 토큰을 발행한다.")
    void generateToken() {
        // given
        String payload = "0x1234abcd";

        // when
        String token = jwtProvider.generateToken(payload);

        // then
        System.out.println(token);
    }
}