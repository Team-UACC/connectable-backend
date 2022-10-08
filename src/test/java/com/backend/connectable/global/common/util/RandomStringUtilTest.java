package com.backend.connectable.global.common.util;

import com.backend.connectable.global.util.RandomStringUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RandomStringUtilTest {

    @DisplayName("RandomStringUtil로 20자의 RandomString을 만들 수 있다.")
    @Test
    void generateRandomString() {
        String randomString = RandomStringUtil.generate();
        System.out.println("randomString = " + randomString);
        assertThat(randomString).hasSize(20);
    }
}
