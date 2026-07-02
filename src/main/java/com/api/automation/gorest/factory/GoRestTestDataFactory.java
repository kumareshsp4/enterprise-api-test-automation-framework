package com.api.automation.gorest.factory;

import com.api.automation.core.utils.RandomDataUtils;
import com.api.automation.gorest.model.request.CreatePostRequest;
import com.api.automation.gorest.model.request.CreateUserRequest;
import com.api.automation.gorest.model.request.UpdatePostRequest;
import com.api.automation.gorest.model.request.UpdateUserRequest;

public final class GoRestTestDataFactory {

    private GoRestTestDataFactory() {
    }

    public static CreateUserRequest createActiveUser() {
        return CreateUserRequest.builder()
                .name(RandomDataUtils.uniqueName())
                .email(RandomDataUtils.uniqueEmail())
                .gender("male")
                .status("active")
                .build();
    }

    public static UpdateUserRequest createUserUpdate() {
        return UpdateUserRequest.builder()
                .name("Updated " + RandomDataUtils.uniqueName())
                .status("inactive")
                .build();
    }

    public static CreatePostRequest createPost() {
        String suffix = RandomDataUtils.uniqueSuffix();

        return CreatePostRequest.builder()
                .title("API automation post " + suffix)
                .body("Created through the scalable GoRest API client.")
                .build();
    }

    public static UpdatePostRequest createPostUpdate() {
        String suffix = RandomDataUtils.uniqueSuffix();

        return UpdatePostRequest.builder()
                .title("Updated API automation post " + suffix)
                .body("Updated through the GoRest API client.")
                .build();
    }
}