package com.api.automation.gorest.client;

import com.api.automation.core.client.BaseApiClient;
import com.api.automation.core.specification.RequestSpecFactory;
import com.api.automation.gorest.endpoint.GoRestEndpoints;
import com.api.automation.gorest.model.request.CreateUserRequest;
import com.api.automation.gorest.model.request.UpdateUserRequest;
import io.restassured.response.Response;

public final class UserApiClient extends BaseApiClient {

    public Response createUser(CreateUserRequest request) {
        return post(
                RequestSpecFactory.goRestAuthenticatedRequest(),
                GoRestEndpoints.USERS,
                request
        );
    }

    public Response createUserWithoutAuthentication(
            CreateUserRequest request
    ) {
        return post(
                RequestSpecFactory.goRestRequest(),
                GoRestEndpoints.USERS,
                request
        );
    }

    public Response getUser(long userId) {
        return get(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("userId", userId),
                GoRestEndpoints.USER_BY_ID
        );
    }

    public Response searchUsersByEmail(String email) {
        return get(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .queryParam("email", email),
                GoRestEndpoints.USERS
        );
    }

    public Response updateUser(
            long userId,
            UpdateUserRequest request
    ) {
        return patch(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("userId", userId),
                GoRestEndpoints.USER_BY_ID,
                request
        );
    }

    public Response replaceUser(
            long userId,
            CreateUserRequest request
    ) {
        return put(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("userId", userId),
                GoRestEndpoints.USER_BY_ID,
                request
        );
    }

    public Response deleteUser(long userId) {
        return delete(
                RequestSpecFactory.goRestAuthenticatedRequest()
                        .pathParam("userId", userId),
                GoRestEndpoints.USER_BY_ID
        );
    }
}