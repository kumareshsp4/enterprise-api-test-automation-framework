package com.api.automation.gorest.client;

import com.api.automation.core.client.BaseApiClient;
import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.gorest.endpoint.GoRestEndpoints;
import com.api.automation.gorest.model.request.CreatePostRequest;
import com.api.automation.gorest.model.request.UpdatePostRequest;
import io.restassured.response.Response;

public final class PostApiClient extends BaseApiClient {

    public Response createPostForUser(
            long userId,
            CreatePostRequest request
    ) {
        return post(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("userId", userId),
                GoRestEndpoints.USER_POSTS,
                request
        );
    }

    public Response getPost(long postId) {
        return get(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("postId", postId),
                GoRestEndpoints.POST_BY_ID
        );
    }

    public Response updatePost(
            long postId,
            UpdatePostRequest request
    ) {
        return patch(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("postId", postId),
                GoRestEndpoints.POST_BY_ID,
                request
        );
    }

    public Response replacePost(
            long postId,
            CreatePostRequest request
    ) {
        return put(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("postId", postId),
                GoRestEndpoints.POST_BY_ID,
                request
        );
    }

    public Response deletePost(long postId) {
        return delete(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("postId", postId),
                GoRestEndpoints.POST_BY_ID
        );
    }
}