package com.api.automation.tests.petstore;

import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.core.specification.ResponseSpecFactory;
import com.api.automation.petstore.endpoint.PetStoreEndpoints;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PetStoreSmokeTest {

    @Test(groups = {"smoke", "petstore"})
    public void shouldRetrieveAvailablePets() {

        given()
                .spec(RequestSpecFactory.petStoreRequest())
                .queryParam("status", "available")
                .when()
                .get(PetStoreEndpoints.PETS_BY_STATUS)
                .then()
                .log().ifValidationFails()
                .spec(ResponseSpecFactory.okJsonResponse())
                .body("$", notNullValue());
    }
}