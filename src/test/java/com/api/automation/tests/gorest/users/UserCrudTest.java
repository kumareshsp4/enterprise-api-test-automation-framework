package com.api.automation.tests.gorest.users;

import com.api.automation.gorest.client.UserApiClient;
import com.api.automation.gorest.factory.GoRestTestDataFactory;
import com.api.automation.gorest.model.request.CreateUserRequest;
import com.api.automation.gorest.model.request.UpdateUserRequest;
import com.api.automation.gorest.model.response.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Enterprise API Automation")
@Feature("GoRest User API")
@Owner("Kumaresan Sitheswaran")
public class UserCrudTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @Test
    @Story("User CRUD Workflow")
    @Description("Validates complete GoRest user lifecycle: create, retrieve, update, delete, and verify 404 after deletion.")
    public void shouldCompleteUserCrudWorkflow() {
        CreateUserRequest createRequest =
                GoRestTestDataFactory.createActiveUser();

        long userId = 0;
        boolean userDeleted = false;

        try {
            Response createResponse =
                    createUser(createRequest);

            validateCreateUserResponse(createResponse, createRequest);

            UserResponse createdUser =
                    createResponse.as(UserResponse.class);

            userId = createdUser.getId();

            Response getResponse =
                    retrieveUser(userId);

            validateRetrievedUserResponse(
                    getResponse,
                    userId,
                    createRequest
            );

            UpdateUserRequest updateRequest =
                    GoRestTestDataFactory.createUserUpdate();

            Response updateResponse =
                    updateUser(userId, updateRequest);

            validateUpdatedUserResponse(
                    updateResponse,
                    userId,
                    updateRequest
            );

            Response deleteResponse =
                    deleteUser(userId);

            validateUserDeleted(deleteResponse);

            userDeleted = true;

            Response deletedUserResponse =
                    retrieveUser(userId);

            validateDeletedUserNotFound(deletedUserResponse);

        } finally {
            if (!userDeleted && userId > 0) {
                cleanupUser(userId);
            }
        }
    }

    @Step("Create GoRest user")
    private Response createUser(CreateUserRequest request) {
        return userApiClient.createUser(request);
    }

    @Step("Validate created user response")
    private void validateCreateUserResponse(
            Response response,
            CreateUserRequest request
    ) {
        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(request.getName()))
                .body("email", equalTo(request.getEmail()))
                .body("gender", equalTo(request.getGender()))
                .body("status", equalTo(request.getStatus()));
    }

    @Step("Retrieve GoRest user with ID: {userId}")
    private Response retrieveUser(long userId) {
        return userApiClient.getUser(userId);
    }

    @Step("Validate retrieved user response")
    private void validateRetrievedUserResponse(
            Response response,
            long userId,
            CreateUserRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("id", equalTo((int) userId))
                .body("name", equalTo(request.getName()))
                .body("email", equalTo(request.getEmail()))
                .body("gender", equalTo(request.getGender()))
                .body("status", equalTo(request.getStatus()));
    }

    @Step("Update GoRest user with ID: {userId}")
    private Response updateUser(
            long userId,
            UpdateUserRequest request
    ) {
        return userApiClient.updateUser(userId, request);
    }

    @Step("Validate updated user response")
    private void validateUpdatedUserResponse(
            Response response,
            long userId,
            UpdateUserRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("id", equalTo((int) userId))
                .body("name", equalTo(request.getName()))
                .body("status", equalTo(request.getStatus()));
    }

    @Step("Delete GoRest user with ID: {userId}")
    private Response deleteUser(long userId) {
        return userApiClient.deleteUser(userId);
    }

    @Step("Validate user delete response")
    private void validateUserDeleted(Response response) {
        response.then()
                .statusCode(204);
    }

    @Step("Validate deleted user returns 404")
    private void validateDeletedUserNotFound(Response response) {
        response.then()
                .statusCode(404);
    }

    @Step("Cleanup GoRest user with ID: {userId}")
    private void cleanupUser(long userId) {
        try {
            Response cleanupResponse =
                    userApiClient.deleteUser(userId);

            int statusCode = cleanupResponse.statusCode();

            if (statusCode != 204 && statusCode != 404) {
                System.err.printf(
                        "User cleanup returned status %d for user ID %d%n",
                        statusCode,
                        userId
                );
            }
        } catch (Exception exception) {
            System.err.printf(
                    "User cleanup failed for user ID %d: %s%n",
                    userId,
                    exception.getMessage()
            );
        }
    }
}