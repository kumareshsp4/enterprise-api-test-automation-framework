package com.api.automation.tests.gorest.users;

import com.api.automation.gorest.client.UserApiClient;
import com.api.automation.gorest.factory.GoRestTestDataFactory;
import com.api.automation.gorest.model.request.CreateUserRequest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;

public class UserNegativeTest {

    private final UserApiClient userApiClient =
            new UserApiClient();

    @Test(
            groups = {"regression", "negative", "gorest"},
            description = "Verify that user creation requires authentication"
    )
    public void shouldRejectUserCreationWithoutToken() {
        CreateUserRequest request =
                GoRestTestDataFactory.createActiveUser();

        userApiClient.createUserWithoutAuthentication(request)
                .then()
                .log().ifValidationFails()
                .statusCode(401);
    }

    @Test(
            groups = {"regression", "negative", "gorest"},
            description = "Verify user payload validation"
    )
    public void shouldRejectInvalidUserPayload() {
        CreateUserRequest invalidRequest =
                new CreateUserRequest(
                        "",
                        "invalid-email",
                        "unsupported",
                        "unsupported"
                );

        userApiClient.createUser(invalidRequest)
                .then()
                .log().ifValidationFails()
                .statusCode(422)
                .body("size()", greaterThan(0))
                .body("field", hasItem("email"));
    }
}