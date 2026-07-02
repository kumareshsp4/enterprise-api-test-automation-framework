package com.api.automation.tests.gorest.posts;

import com.api.automation.gorest.client.PostApiClient;
import com.api.automation.gorest.client.UserApiClient;
import com.api.automation.gorest.factory.GoRestTestDataFactory;
import com.api.automation.gorest.model.request.CreatePostRequest;
import com.api.automation.gorest.model.request.CreateUserRequest;
import com.api.automation.gorest.model.request.UpdatePostRequest;
import com.api.automation.gorest.model.response.PostResponse;
import com.api.automation.gorest.model.response.UserResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostCrudTest {

    private final UserApiClient userApiClient =
            new UserApiClient();

    private final PostApiClient postApiClient =
            new PostApiClient();

    @Test(
            groups = {"regression", "gorest", "posts"},
            description = "Verify the complete GoRest post lifecycle"
    )
    public void shouldCompletePostCrudWorkflow() {
        long userId = 0;
        long postId = 0;

        try {
            CreateUserRequest userRequest =
                    GoRestTestDataFactory.createActiveUser();

            Response userResponse =
                    userApiClient.createUser(userRequest);

            userResponse.then()
                    .log().ifValidationFails()
                    .statusCode(201);

            UserResponse createdUser =
                    userResponse.as(UserResponse.class);

            userId = createdUser.getId();

            CreatePostRequest postRequest =
                    GoRestTestDataFactory.createPost();

            Response createPostResponse =
                    postApiClient.createPostForUser(
                            userId,
                            postRequest
                    );

            createPostResponse.then()
                    .log().ifValidationFails()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("user_id", equalTo((int) userId))
                    .body("title", equalTo(postRequest.getTitle()))
                    .body("body", equalTo(postRequest.getBody()));

            PostResponse createdPost =
                    createPostResponse.as(PostResponse.class);

            postId = createdPost.getId();

            postApiClient.getPost(postId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("id", equalTo((int) postId))
                    .body("user_id", equalTo((int) userId));

            UpdatePostRequest updateRequest =
                    GoRestTestDataFactory.createPostUpdate();

            postApiClient.updatePost(postId, updateRequest)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("title", equalTo(updateRequest.getTitle()))
                    .body("body", equalTo(updateRequest.getBody()));

            postApiClient.deletePost(postId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(204);

            postApiClient.getPost(postId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(404);

            postId = 0;

        } finally {
            if (postId > 0) {
                postApiClient.deletePost(postId);
            }

            if (userId > 0) {
                userApiClient.deleteUser(userId);
            }
        }
    }
}