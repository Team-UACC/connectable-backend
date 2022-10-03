package com.backend.connectable.global.redis;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisRepositoryTest {

    @Autowired RedisRepository redisRepository;

    @DisplayName("레디스에 데이터를 저장하여 조회할 수 있다.")
    @Test
    void setData() {
        // given
        String key = "key";
        String value = "value";
        redisRepository.setData(key, value);

        // when
        String data = redisRepository.getData(key);

        // then
        assertThat(data).isEqualTo(value);
    }

    @DisplayName("레디스에 데이터를 만료 시간을 정하여 조회할 수 있다.")
    @Test
    void setDataExpire() {
        // given
        String key = "key";
        String value = "value";
        redisRepository.setDataExpire(key, value, 1);

        // when & then
        String dataRightAfter = redisRepository.getData(key);
        assertThat(dataRightAfter).isEqualTo(value);

        await().atMost(1500, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            return (redisRepository.getData(key) == null);
                        });
    }
}
