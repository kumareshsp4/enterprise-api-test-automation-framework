package com.api.automation.tests.petstore;

import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.petstore.endpoint.PetStoreEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Enterprise API Automation")
@Feature("Petstore API")
@Owner("Kumaresan Sitheswaran")
public class PetStoreSmokeTest {

    @Test
    @Story("Petstore Smoke Validation")
    @Description("Validates that the Petstore find-by-status endpoint is available and returns available pets.")
    public void shouldRetrieveAvailablePets() {
        Response response = retrieveAvailablePets();

        validateAvailablePetsResponse(response);
    }

    @Step("Retrieve available pets from Petstore")
    private Response retrieveAvailablePets() {
        return given()
                .spec(RequestSpecFactory.petStoreRequest())
                .queryParam("status", "available")
                .when()
                .get(PetStoreEndpoints.PETS_BY_STATUS);
    }

    @Step("Validate available pets response")
    private void validateAvailablePetsResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("$", notNullValue());
    }
}