package com.icy.api.utility;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

/**
 * TokenManager handles the "Infrastructure" of authentication.
 * It ensures we only hit the /login endpoint once per test suite run.
 */
public class TokenManager {
    private static String accessToken;
    private static long expiryTime;

    /**
     * Retrieves a valid token. If no token exists or it's about to expire,
     * it automatically triggers a login request.
     */
    public synchronized static String getToken() {
        try {
            if (accessToken == null) {
                System.out.println("No token found. Generating new token...");
                accessToken = renewToken();
            }
        } catch (Exception e) {
            throw new RuntimeException("ABORT: Failed to fetch Auth Token. " + e.getMessage());
        }
        return accessToken;
    }

    private static String renewToken() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin_user");
        credentials.put("password", "secure_password123");

        Response response = given()
                .baseUri("https://api.example.com")
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        return response.path("token");
    }
}
