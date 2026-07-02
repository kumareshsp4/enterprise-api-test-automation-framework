package com.api.automation.core.authentication;

import com.api.automation.core.exception.ConfigurationException;

public final class BearerTokenProvider {

    private static final String TOKEN_SYSTEM_PROPERTY = "gorest.token";
    private static final String TOKEN_ENVIRONMENT_VARIABLE = "GOREST_TOKEN";

    private BearerTokenProvider() {
        // Utility class.
    }

    public static String getGoRestToken() {
        String token = System.getProperty(TOKEN_SYSTEM_PROPERTY);

        if (token == null || token.isBlank()) {
            token = System.getenv(TOKEN_ENVIRONMENT_VARIABLE);
        }

        if (token == null || token.isBlank()) {
            throw new ConfigurationException(
                    "GoRest token is missing. Set the GOREST_TOKEN " +
                            "environment variable or pass -Dgorest.token."
            );
        }

        return token.trim();
    }
}