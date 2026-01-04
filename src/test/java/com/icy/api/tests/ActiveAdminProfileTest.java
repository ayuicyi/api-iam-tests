package com.icy.api.tests;

import com.icy.api.utility.BaseSpecManager;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Realistic Positive Scenarios for IAM API.
 * Focuses on Business Logic validation and Data Integrity.
 */
public class ActiveAdminProfileTest {

    private final String activeUser = "admin.manager@icycorp.com";

    /**
     * SCENARIO: Verify Active Administrator Profile
     * A realistic test ensures that a high-privileged user
     * returns specific metadata required by the frontend application.
     */
    @Test
    public void verifyAdminUserMetadataAndState() {
        given()
                .spec(BaseSpecManager.getIAMRequestSpec())
                .when()
                .get("/operators/" + activeUser)
                .then()
                // 1. Connection & Protocol
                .statusCode(200)
                .header("Cache-Control", containsString("no-store")) // Security check: IAM data shouldn't be cached

                // 2. Business Logic: Status & Account State
                .body("accountStatus", equalTo("ACTIVE"))
                .body("isLocked", equalTo(false))
                .body("lastLoginTimestamp", notNullValue())

                // 3. Organizational Hierarchy Validation
                .body("orgHierarchy.company", equalTo("ICY-CORP"))
                .body("orgHierarchy.division", equalTo("Engineering"))
                .body("orgHierarchy.unit", equalTo("Platform-Team"))

                // 4. Role-Based Access (The most important positive check)
                // We check that the user HAS 'SuperAdmin' but DOES NOT have 'Guest'
                .body("roles.name", hasItems("SuperAdmin", "ReportsUser"))
                .body("roles.name", not(hasItem("GuestUser")));
    }

    /**
     * SCENARIO: Validate Response Schema Consistency
     * Ensures that even if the data changes, the keys and data types
     * remain consistent for the consuming frontend.
     */
    @Test
    public void verifyUserPreferencesArePresent() {
        given()
                .spec(BaseSpecManager.getIAMRequestSpec())
                .when()
                .get("/operators/" + activeUser + "/preferences")
                .then()
                .statusCode(200)
                .body("timezone", matchesPattern("^[A-Z]{3,}/[A-Z][a-z_]+$")) // e.g., America/New_York
                .body("language", equalTo("en-US"))
                .body("notifications.emailEnabled", isA(Boolean.class));
    }
}