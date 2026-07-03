package com.api.automation.core.specification;

import com.api.automation.core.authentication.BearerTokenProvider;
import com.api.automation.core.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private RequestSpecFactory() {
        // Utility class.
    }

    public static RequestSpecification goRestRequest() {
        return commonRequestBuilder(
                ConfigManager.get("gorest.base.url")
        ).build();
    }

    public static RequestSpecification goRestAuthenticatedRequest() {
        return commonRequestBuilder(
                ConfigManager.get("gorest.base.url")
        )
                .addHeader(
                        "Authorization",
                        "Bearer " + BearerTokenProvider.getGoRestToken()
                )
                .build();
    }

    public static RequestSpecification petStoreRequest() {
        return commonRequestBuilder(
                ConfigManager.get("petstore.base.url")
        ).  build();
    }

    private static RequestSpecBuilder commonRequestBuilder(
            String baseUrl) {

        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .addFilter(new ErrorLoggingFilter());
    }
}