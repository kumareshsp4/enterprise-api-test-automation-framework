package com.api.automation.core.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected Response get(RequestSpecification requestSpecification, String endpoint)
    {
        return given()
                .spec(requestSpecification)
                .when()
                .get(endpoint);
    }

    protected Response post(RequestSpecification requestSpecification, String endpoint, Object requestBody )
    {
        return given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post(endpoint);
    }

    protected Response put(RequestSpecification requestSpecification, String endpoint, Object requestBody)
    {
        return given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .put(endpoint);
    }

    protected Response patch(RequestSpecification requestSpecification,String endpoint,Object requestBody)
    {
        return given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .patch(endpoint);
    }

    protected Response delete(RequestSpecification requestSpecification,String endpoint)
    {
        return given()
                .spec(requestSpecification)
                .when()
                .delete(endpoint);
    }
}