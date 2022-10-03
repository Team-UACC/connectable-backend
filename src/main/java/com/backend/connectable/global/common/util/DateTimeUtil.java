package com.backend.connectable.global.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * LocalDateTime 을 Epoch Micro Milliseconds로 변환
     * @param dateTime
     * @return long
     */
    public static Long toEpochMilliSeconds(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;
    }
}
