package com.api.automation.tests.gorest.users;

import com.api.automation.gorest.client.UserApiClient;
import com.api.automation.gorest.factory.GoRestTestDataFactory;
import com.api.automation.gorest.model.request.CreateUserRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@Epic("Enterprise API Automation")
@Feature("GoRest User API")
@Owner("Kumaresan Sitheswaran")
public class UserNegativeTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @Test
    @Story("Authentication Validation")
    @Description("Validates that GoRest rejects user creation when bearer token is missing.")
    public void shouldRejectUserCreationWithoutToken() {
        CreateUserRequest request =
                GoRestTestDataFactory.createActiveUser();

        Response response =
                createUserWithoutAuthentication(request);

        validateUnauthorizedResponse(response);
    }

    @Test
    @Story("Payload Validation")
    @Description("Validates that GoRest rejects invalid user payload with validation errors.")
    public void shouldRejectInvalidUserPayload() {
        CreateUserRequest invalidRequest =
                CreateUserRequest.builder()
                        .name("")
                        .email("invalid-email")
                        .gender("unknown")
                        .status("invalid")
                        .build();

        Response response =
                createUserWithInvalidPayload(invalidRequest);

        validateInvalidPayloadResponse(response);
    }

    @Step("Create GoRest user without authentication")
    private Response createUserWithoutAuthentication(
            CreateUserRequest request
    ) {
        return userApiClient.createUserWithoutAuthentication(request);
    }

    @Step("Validate unauthorized response")
    private void validateUnauthorizedResponse(Response response) {
        response.then()
                .statusCode(401)
                .body("message", equalTo("Authentication failed"));
    }

    @Step("Create GoRest user with invalid payload")
    private Response createUserWithInvalidPayload(
            CreateUserRequest request
    ) {
        return userApiClient.createUser(request);
    }

    @Step("Validate invalid payload response")
    private void validateInvalidPayloadResponse(Response response) {
        response.then()
                .statusCode(422)
                .body("size()", greaterThan(0));
    }
}