package com.icy.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.icy.api.tests.models.UserTestData;
import com.icy.api.utility.CSVUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;

/**
 * UserApiTest
 * Validates API responses based on the 'expectedStatus' field from the CSV data.
 */
public class UserApiTest {

    @BeforeClass
    public void setup() {
        // Set the base URI for the API
        RestAssured.baseURI = "https://api.example.com";
    }

    /**
     * DataProvider to fetch records from the CSV file via your utility class.
     */
    @DataProvider(name = "userDataProvider")
    public Object[][] getData() {
        // Update the path to your actual iamREAL.csv location
        List<UserTestData> users = CSVUtils.readUserTestData("src/test/resources/iamREAL.csv");

        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }

    /**
     * Test execution: Replaces the 'id' check with dynamic status code validation.
     */
    @Test(dataProvider = "userDataProvider")
    public void testUserCreation(UserTestData user) {
        int expectedStatusCode = user.getExpectedStatus();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/iam/endpoint") // Replace with actual endpoint
                .then()
                .log().ifValidationFails()
                .statusCode(expectedStatusCode)
                .extract().response();

        // Only validate body content if the expected status is in the 2xx (Success) range
        if (expectedStatusCode >= 200 && expectedStatusCode < 300) {
            response.then().body("username", equalTo(user.getUsername()));
            response.then().body("role", equalTo(user.getRole()));
        }

        System.out.println("Validated: " + user.getUsername() + " | Role: " + user.getRole() + " | Expected Status: " + expectedStatusCode);
    }
}