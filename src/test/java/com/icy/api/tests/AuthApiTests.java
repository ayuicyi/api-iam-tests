package com.icy.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Focus: Identity Verification
 * Validates that users can authenticate and obtain access tokens.
 */
public class AuthApiTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.your-system.com";
    }

    @Test
    @DisplayName("GIVEN valid credentials WHEN user logs in THEN token is returned")
    public void testSuccessfulLogin() {
        // Step 8 dependency: Use the user credentials created previously
        String loginPayload = "{ \"username\": \"testuser_step8\", \"password\": \"SecurePass123!\" }";

        given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("tokenType", equalTo("Bearer"))
                .log().ifValidationFails();
    }

    @Test
    @DisplayName("GIVEN invalid password WHEN user logs in THEN unauthorized error")
    public void testInvalidLogin() {
        String invalidPayload = "{ \"username\": \"testuser_step8\", \"password\": \"WrongPassword\" }";

        given()
                .contentType(ContentType.JSON)
                .body(invalidPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"))
                .body("message", containsString("Invalid credentials"));
    }
}
