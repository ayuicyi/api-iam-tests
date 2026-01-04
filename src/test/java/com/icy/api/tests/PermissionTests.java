package com.icy.api.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Focus: Authorization & Access Control
 * Validates that the IAM system correctly enforces permissions.
 */
public class PermissionTests {

    private static String userToken;
    private static String adminToken;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.your-system.com";

        // In a real scenario, these would be retrieved from your
        // Shared Authentication Service built in Step 8.
        userToken = "dummy-token-from-step-8-user";
        adminToken = "dummy-token-for-admin";
    }

    @Test
    @DisplayName("GIVEN a standard user WHEN accessing admin endpoints THEN status is 403 Forbidden")
    public void testStandardUserCannotAccessAdminData() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get("/api/v1/iam/admin/manage-users")
                .then()
                .statusCode(403)
                .body("code", equalTo("ACCESS_DENIED"))
                .log().all();
    }

    @Test
    @DisplayName("GIVEN an authenticated user WHEN checking permissions THEN list contains 'READ' rights")
    public void testUserHasCorrectPermissions() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get("/api/v1/iam/permissions/me")
                .then()
                .statusCode(200)
                .body("permissions", hasItem("READ_FILES"))
                .body("permissions", not(hasItem("DELETE_USER")));
    }

    @Test
    @DisplayName("GIVEN no token WHEN accessing IAM endpoints THEN status is 401 Unauthorized")
    public void testUnauthenticatedAccess() {
        given()
                .when()
                .get("/api/v1/iam/permissions/me")
                .then()
                .statusCode(401);
    }
}
