package com.icy.api.tests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.icy.api.utility.BaseSpecManager;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**to verify that a specific user exists in the system and that their account is "Active.
 * " In a real-world scenario, you would run this test to ensure your Identity and
 * Access Management (IAM) system is correctly reporting user states.
 */


public class IAMUserTests {

    @Test
    public void verifyUserStatus() {
        given()
                .spec(BaseSpecManager.getIAMRequestSpec())
                .when()
                .get("/operators/test.user@company.com")
                .then()
                .statusCode(200)
                .body("pyStatus", equalTo("Active"));
    }
}
