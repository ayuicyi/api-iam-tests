package com.icy.api.utility;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TokenManager {
    // Shared variable to store the token once it's fetched
    private static String accessToken;

    /**
     * Shared method for all tests.
     * It uses your ConfigReader to get credentials from application.properties.
     */
    public static synchronized String getToken() {
        if (accessToken == null) {
            System.out.println("[SHARED LOG] No token found. Initializing login...");
            accessToken = fetchNewToken();
            System.out.println("[SHARED LOG] New token acquired and stored for shared use.");
        } else {
            System.out.println("[SHARED LOG] Reusing existing shared token.");
        }
        return accessToken;
    }

    private static String fetchNewToken() {
        // We call ConfigReader.getInstance() because of your specific script structure
        ConfigReader config = ConfigReader.getInstance();

        Response response = RestAssured.given()
                .baseUri(config.getProperty("iam.auth.url"))
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "password")
                .formParam("client_id", config.getProperty("iam.client.id"))
                .formParam("username", config.getProperty("iam.username"))
                .formParam("password", config.getProperty("iam.password"))
                .post(config.getProperty("iam.login.path"));

        if (response.statusCode() != 200) {
            throw new RuntimeException("CRITICAL: IAM Login failed! Status: " + response.statusCode()
                    + " Body: " + response.getBody().asString());
        }

        return response.jsonPath().getString("access_token");
    }

    // Optional: Method to force refresh the token if it expires
    public static void expireToken() {
        accessToken = null;
    }
}