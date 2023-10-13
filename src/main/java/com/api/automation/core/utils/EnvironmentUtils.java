package com.api.automation.core.utils;

public final class EnvironmentUtils {

    public static String currentEnvironment() {
        return System.getProperty("env", "qa");
    }

    public static boolean isCi() {
        return Boolean.parseBoolean(
                System.getenv().getOrDefault("CI", "false")
        );
    }
}