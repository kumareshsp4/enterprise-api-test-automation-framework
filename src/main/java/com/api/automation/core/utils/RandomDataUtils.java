package com.api.automation.core.utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public final class RandomDataUtils {

    private RandomDataUtils() {
        // Utility class.
    }
    private static final AtomicLong UNIQUE_ID =
            new AtomicLong(System.currentTimeMillis());

    static Random random = new Random();

    public static String uniqueSuffix() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);
    }

    public static String uniqueEmail() {
        return "api.automation."
                + uniqueSuffix()
                + "@example.com";
    }

    public static String uniqueName() {
        return "Automation User "
                + uniqueSuffix();
    }


    public static int uniqueNumber() {
        return random.nextInt(100);
    }
}