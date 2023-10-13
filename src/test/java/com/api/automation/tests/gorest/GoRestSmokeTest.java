package com.api.automation.tests.gorest;

import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.core.specification.ResponseSpecFactory;
import com.api.automation.gorest.endpoint.GoRestEndpoints;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GoRestSmokeTest {

    @Test(groups = {"smoke", "gorest"})
    public void shouldRetrieveGoRestUsers() {

        given()
                .spec(RequestSpecFactory.goRestRequest())
                .when()
                .get(GoRestEndpoints.USERS)
                .then()
                .log().ifValidationFails()
                .spec(ResponseSpecFactory.okJsonResponse())
                .body("$", notNullValue());
    }
}