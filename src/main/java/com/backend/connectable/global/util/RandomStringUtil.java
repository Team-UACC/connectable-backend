package com.backend.connectable.global.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtil {

    private static final int RANDOM_STRING_LENGTH = 20;

    private RandomStringUtil() {}

    public static String generate() {
        return RandomStringUtils.randomAlphabetic(RANDOM_STRING_LENGTH);
    }
}
