package com.api.automation.core.utils;

import java.util.UUID;

public final class RandomDataUtils {

    private RandomDataUtils() {
        // Utility class.
    }

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
}