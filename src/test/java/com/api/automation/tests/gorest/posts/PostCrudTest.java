package com.api.automation.tests.gorest.posts;

import com.api.automation.gorest.client.PostApiClient;
import com.api.automation.gorest.client.UserApiClient;
import com.api.automation.gorest.factory.GoRestTestDataFactory;
import com.api.automation.gorest.model.request.CreatePostRequest;
import com.api.automation.gorest.model.request.CreateUserRequest;
import com.api.automation.gorest.model.request.UpdatePostRequest;
import com.api.automation.gorest.model.response.PostResponse;
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
@Feature("GoRest Post API")
@Owner("Kumaresan Sitheswaran")
public class PostCrudTest {

    private final UserApiClient userApiClient = new UserApiClient();
    private final PostApiClient postApiClient = new PostApiClient();

    @Test
    @Story("Post CRUD Workflow")
    @Description("Validates complete GoRest post lifecycle using a dynamically created user.")
    public void shouldCompletePostCrudWorkflow() {
        long userId = 0;
        long postId = 0;

        boolean userDeleted = false;
        boolean postDeleted = false;

        try {
            CreateUserRequest createUserRequest =
                    GoRestTestDataFactory.createActiveUser();

            Response createUserResponse =
                    createUser(createUserRequest);

            validateCreateUserResponse(createUserResponse);

            UserResponse createdUser =
                    createUserResponse.as(UserResponse.class);

            userId = createdUser.getId();

            CreatePostRequest createPostRequest =
                    GoRestTestDataFactory.createPost();

            Response createPostResponse =
                    createPostForUser(userId, createPostRequest);

            validateCreatePostResponse(
                    createPostResponse,
                    userId,
                    createPostRequest
            );

            PostResponse createdPost =
                    createPostResponse.as(PostResponse.class);

            postId = createdPost.getId();

            Response getPostResponse =
                    retrievePost(postId);

            validateRetrievedPostResponse(
                    getPostResponse,
                    postId,
                    userId,
                    createPostRequest
            );

            UpdatePostRequest updatePostRequest =
                    GoRestTestDataFactory.createPostUpdate();

            Response updatePostResponse =
                    updatePost(postId, updatePostRequest);

            validateUpdatedPostResponse(
                    updatePostResponse,
                    postId,
                    updatePostRequest
            );

            Response deletePostResponse =
                    deletePost(postId);

            validatePostDeleted(deletePostResponse);

            postDeleted = true;

            Response deletedPostResponse =
                    retrievePost(postId);

            validateDeletedPostNotFound(deletedPostResponse);

            Response deleteUserResponse =
                    deleteUser(userId);

            validateUserDeleted(deleteUserResponse);

            userDeleted = true;

        } finally {
            if (!postDeleted && postId > 0) {
                cleanupPost(postId);
            }

            if (!userDeleted && userId > 0) {
                cleanupUser(userId);
            }
        }
    }

    @Step("Create GoRest user for post workflow")
    private Response createUser(CreateUserRequest request) {
        return userApiClient.createUser(request);
    }

    @Step("Validate GoRest user creation for post workflow")
    private void validateCreateUserResponse(Response response) {
        response.then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Step("Create GoRest post for user ID: {userId}")
    private Response createPostForUser(
            long userId,
            CreatePostRequest request
    ) {
        return postApiClient.createPostForUser(userId, request);
    }

    @Step("Validate created GoRest post response")
    private void validateCreatePostResponse(
            Response response,
            long userId,
            CreatePostRequest request
    ) {
        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("user_id", equalTo((int) userId))
                .body("title", equalTo(request.getTitle()))
                .body("body", equalTo(request.getBody()));
    }

    @Step("Retrieve GoRest post with ID: {postId}")
    private Response retrievePost(long postId) {
        return postApiClient.getPost(postId);
    }

    @Step("Validate retrieved GoRest post response")
    private void validateRetrievedPostResponse(
            Response response,
            long postId,
            long userId,
            CreatePostRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("id", equalTo((int) postId))
                .body("user_id", equalTo((int) userId))
                .body("title", equalTo(request.getTitle()))
                .body("body", equalTo(request.getBody()));
    }

    @Step("Update GoRest post with ID: {postId}")
    private Response updatePost(
            long postId,
            UpdatePostRequest request
    ) {
        return postApiClient.updatePost(postId, request);
    }

    @Step("Validate updated GoRest post response")
    private void validateUpdatedPostResponse(
            Response response,
            long postId,
            UpdatePostRequest request
    ) {
        response.then()
                .statusCode(200)
                .body("id", equalTo((int) postId))
                .body("title", equalTo(request.getTitle()))
                .body("body", equalTo(request.getBody()));
    }

    @Step("Delete GoRest post with ID: {postId}")
    private Response deletePost(long postId) {
        return postApiClient.deletePost(postId);
    }

    @Step("Validate GoRest post delete response")
    private void validatePostDeleted(Response response) {
        response.then()
                .statusCode(204);
    }

    @Step("Validate deleted GoRest post returns 404")
    private void validateDeletedPostNotFound(Response response) {
        response.then()
                .statusCode(404);
    }

    @Step("Delete GoRest user with ID: {userId}")
    private Response deleteUser(long userId) {
        return userApiClient.deleteUser(userId);
    }

    @Step("Validate GoRest user delete response")
    private void validateUserDeleted(Response response) {
        response.then()
                .statusCode(204);
    }

    @Step("Cleanup GoRest post with ID: {postId}")
    private void cleanupPost(long postId) {
        try {
            Response cleanupResponse =
                    postApiClient.deletePost(postId);

            int statusCode = cleanupResponse.statusCode();

            if (statusCode != 204 && statusCode != 404) {
                System.err.printf(
                        "Post cleanup returned status %d for post ID %d%n",
                        statusCode,
                        postId
                );
            }
        } catch (Exception exception) {
            System.err.printf(
                    "Post cleanup failed for post ID %d: %s%n",
                    postId,
                    exception.getMessage()
            );
        }
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