package com.api.automation.tests.gorest;

import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.gorest.endpoint.GoRestEndpoints;
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
@Feature("GoRest API")
@Owner("Kumaresan Sitheswaran")
public class GoRestSmokeTest {

    @Test
    @Story("GoRest Smoke Validation")
    @Description("Validates that the GoRest users endpoint is available and returns a successful response.")
    public void shouldRetrieveGoRestUsers() {
        Response response = retrieveGoRestUsers();

        validateGoRestUsersResponse(response);
    }

    @Step("Retrieve GoRest users")
    private Response retrieveGoRestUsers() {
        return given()
                .spec(RequestSpecFactory.goRestRequest())
                .when()
                .get(GoRestEndpoints.USERS);
    }

    @Step("Validate GoRest users response")
    private void validateGoRestUsersResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("$", notNullValue());
    }
}