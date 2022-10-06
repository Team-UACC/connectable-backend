package com.backend.connectable.global.common.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.backend.connectable.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @DisplayName("LocalDateTime을 ms로 변환할 수 있다.")
    @Test
    void convertLocalDateTime() {
        // given
        LocalDateTime date1 = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2000, 1, 1, 0, 0, 1);

        // when
        Long date1ToLong = DateTimeUtil.toEpochMilliSeconds(date1);
        Long date2ToLong = DateTimeUtil.toEpochMilliSeconds(date2);

        // then
        assertThat(Math.abs(date1ToLong - date2ToLong)).isEqualTo(1000L);
    }
}
