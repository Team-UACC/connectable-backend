package com.backend.connectable.global.common.util;

import java.util.concurrent.TimeUnit;

public class WaitUtil {

    private WaitUtil() {}

    public static void waitForSecond(int seconds) {
        long waitMilliseconds = TimeUnit.SECONDS.toMillis(seconds);
        long startTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            if (now >= startTime + waitMilliseconds) {
                return;
            }
        }
    }
}
