package com.api.automation.core.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApiClient {

    protected Response get(
            RequestSpecification specification,
            String endpoint) {

        return specification
                .when()
                .get(endpoint);
    }

    protected Response post(
            RequestSpecification specification,
            String endpoint,
            Object payload) {

        return specification
                .body(payload)
                .when()
                .post(endpoint);
    }

    protected Response put(
            RequestSpecification specification,
            String endpoint,
            Object payload) {

        return specification
                .body(payload)
                .when()
                .put(endpoint);
    }

    protected Response patch(
            RequestSpecification specification,
            String endpoint,
            Object payload) {

        return specification
                .body(payload)
                .when()
                .patch(endpoint);
    }

    protected Response delete(
            RequestSpecification specification,
            String endpoint) {

        return specification
                .when()
                .delete(endpoint);
    }
}
