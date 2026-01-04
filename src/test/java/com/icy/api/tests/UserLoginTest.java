package com.icy.api.tests;

import org.testng.annotations.*;
import com.icy.api.utility.DbUtils;
import io.restassured.RestAssured;

public class UserLoginTest {

    @BeforeMethod
    public void setupTestData() {
        // WORKFLOW START: Injecting state via DbUtils
        // We ensure the user is 'Locked' in the DB to test the lockout API
        String sql = "UPDATE iam_users SET status = 'LOCKED' WHERE email = 'test@example.com'";
        DbUtils.executeUpdate(sql);
        System.out.println("Step 1: Database state prepared (User Locked).");
    }

    @Test
    public void testLockedUserLogin() {
        // WORKFLOW MIDDLE: The API Call
        // This is the actual 'Testing' part
        RestAssured.given()
                .body("{ \"email\": \"test@example.com\", \"password\": \"Secret123\" }")
                .post("/login")
                .then()
                .statusCode(403); // Expecting Forbidden

        System.out.println("Step 2: API Call executed and validated.");
    }

    @AfterMethod
    public void tearDown() {
        // WORKFLOW END: Cleaning up via DbUtils
        // Return the user to 'ACTIVE' status so other tests don't fail
        String sql = "UPDATE iam_users SET status = 'ACTIVE' WHERE email = 'test@example.com'";
        DbUtils.executeUpdate(sql);
        System.out.println("Step 3: Database cleaned up.");
    }
}
