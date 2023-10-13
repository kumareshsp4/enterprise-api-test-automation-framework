package com.api.automation.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final String DEFAULT_ENVIRONMENT = "qa";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {
        // Utility class.
    }

    private static Properties loadProperties() {
        String environment = System.getProperty(
                "env",
                DEFAULT_ENVIRONMENT
        ).trim();

        String resourcePath =
                "config/" + environment + ".properties";

        Properties properties = new Properties();

        try (InputStream inputStream =
                     ConfigManager.class
                             .getClassLoader()
                             .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Configuration file was not found: "
                                + resourcePath
                );
            }

            properties.load(inputStream);
            return properties;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load configuration file: "
                            + resourcePath,
                    exception
            );
        }
    }

    public static String get(String key) {
        // Allows command-line overrides such as:
        // -Dgorest.base.url=https://...
        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String configuredValue = PROPERTIES.getProperty(key);

        if (configuredValue == null || configuredValue.isBlank()) {
            throw new IllegalStateException(
                    "Missing configuration value for key: " + key
            );
        }

        return configuredValue.trim();
    }

    public static String getEnvironment() {
        return System.getProperty(
                "env",
                DEFAULT_ENVIRONMENT
        ).trim();
    }
}