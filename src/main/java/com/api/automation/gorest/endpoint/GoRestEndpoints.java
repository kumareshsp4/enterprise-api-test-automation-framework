package com.api.automation.gorest.endpoint;

public final class GoRestEndpoints {

    public static final String USERS = "/users";
    public static final String USER_BY_ID = "/users/{userId}";
    public static final String USER_POSTS = "/users/{userId}/posts";

    public static final String POSTS = "/posts";
    public static final String POST_BY_ID = "/posts/{postId}";

    private GoRestEndpoints() {
        // Utility class.
    }
}