package com.icy.api.services;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthenticationService handles all API interactions related to IAM.
 * It encapsulates the endpoints and request logic to keep tests clean.
 */
public class AuthenticationService {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private static final String LOGOUT_ENDPOINT = "/auth/logout";
    private static final String REFRESH_ENDPOINT = "/auth/refresh";

    /**
     * Attempts to log in a user with a username and password.
     * @param username The user's identification
     * @param password The user's secret password
     * @return The RestAssured Response object
     */
    public Response login(String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    /**
     * Sends a request to invalidate the current session token.
     * @param token The bearer token to invalidate
     * @return The RestAssured Response object
     */
    public Response logout(String token) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post(LOGOUT_ENDPOINT);
    }

    /**
     * Extracts the token from a successful login response.
     * Useful for chaining requests in your tests.
     */
    public String getTokenFromResponse(Response response) {
        return response.jsonPath().getString("accessToken");
    }
}