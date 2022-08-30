package com.backend.connectable.global.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RandomStringUtilTest {

    @DisplayName("RandomStringUtil로 20자의 RandomString을 만들 수 있다.")
    @Test
    void generateRandomString() {
        String randomString = RandomStringUtil.generate();
        assertThat(randomString).hasSize(20);
    }
}
