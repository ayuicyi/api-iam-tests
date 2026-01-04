package com.icy.api.tests;

import com.icy.api.BaseTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Focus: Identity Verification
 * We extend BaseTest to use the centrally managed environment and specs.
 */
public class AuthApiTests extends BaseTest {

    // REMOVED: Redundant @BeforeAll that was hardcoding the URL.
    // The BaseURI is now pulled from configReader inside the parent BaseTest.

    @Test
    @DisplayName("GIVEN valid credentials WHEN user logs in THEN token is returned")
    public void testSuccessfulLogin() {
        String loginPayload = "{ \"username\": \"testuser_step8\", \"password\": \"SecurePass123!\" }";

        given()
                .spec(requestSpec) // This injects the Base URL, JSON Content-Type, and Headers from BaseTest
                .body(loginPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                // You can also use responseSpec here if you want to enforce the status 200 check automatically:
                // .spec(responseSpec)
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
                .spec(requestSpec) // Ensures this test uses the same environment as all others
                .body(invalidPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                // Note: We don't use responseSpec here because we EXPECT a 401, not a 200.
                .statusCode(401)
                .body("error", equalTo("Unauthorized"))
                .body("message", containsString("Invalid credentials"));
    }
}