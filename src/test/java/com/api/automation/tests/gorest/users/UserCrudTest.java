package com.api.automation.tests.gorest.users;

import com.api.automation.gorest.client.UserApiClient;
import com.api.automation.gorest.factory.GoRestTestDataFactory;
import com.api.automation.gorest.model.request.CreateUserRequest;
import com.api.automation.gorest.model.request.UpdateUserRequest;
import com.api.automation.gorest.model.response.UserResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertTrue;

public class UserCrudTest {

    private final UserApiClient userApiClient =
            new UserApiClient();

    @Test(
            groups = {"regression", "gorest", "users"},
            description = "Verify the complete GoRest user lifecycle"
    )
    public void shouldCompleteUserCrudWorkflow() {
        CreateUserRequest createRequest =
                GoRestTestDataFactory.createActiveUser();

        long userId = 0;

        try {
            Response createResponse =
                    userApiClient.createUser(createRequest);

            createResponse.then()
                    .log().ifValidationFails()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("name", equalTo(createRequest.getName()))
                    .body("email", equalTo(createRequest.getEmail()))
                    .body("gender", equalTo(createRequest.getGender()))
                    .body("status", equalTo(createRequest.getStatus()));

            UserResponse createdUser =
                    createResponse.as(UserResponse.class);

            userId = createdUser.getId();
            assertTrue(userId > 0);

            userApiClient.getUser(userId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("id", equalTo((int) userId))
                    .body("email", equalTo(createRequest.getEmail()));

            userApiClient.searchUsersByEmail(createRequest.getEmail())
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .header("X-Pagination-Total", notNullValue())
                    .header("X-Pagination-Pages", notNullValue())
                    .header("X-Pagination-Page", notNullValue())
                    .header("X-Pagination-Limit", notNullValue())
                    .body("email", hasItem(createRequest.getEmail()));

            UpdateUserRequest updateRequest =
                    GoRestTestDataFactory.createUserUpdate();

            userApiClient.updateUser(userId, updateRequest)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("id", equalTo((int) userId))
                    .body("name", equalTo(updateRequest.getName()))
                    .body("status", equalTo(updateRequest.getStatus()));

            userApiClient.deleteUser(userId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(204);

            userApiClient.getUser(userId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(404);

            userId = 0;

        } finally {
            if (userId > 0) {
                userApiClient.deleteUser(userId);
            }
        }
    }
}