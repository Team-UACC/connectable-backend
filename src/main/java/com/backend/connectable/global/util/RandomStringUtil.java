package com.backend.connectable.global.util;

import java.security.SecureRandom;

public class RandomStringUtil {

    private static final int RANDOM_STRING_LENGTH = 20;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final char[] CHAR_SET =
            new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^',
                '&'
            };

    private RandomStringUtil() {}

    public static String generate() {
        StringBuilder sb = new StringBuilder(RANDOM_STRING_LENGTH);
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            sb.append(CHAR_SET[SECURE_RANDOM.nextInt(CHAR_SET.length)]);
        }
        return sb.toString();
    }
}
