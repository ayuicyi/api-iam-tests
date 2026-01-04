package com.icy.api.tests;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Demonstrates a Hybrid Data Strategy for IAM Testing.
 * 1. CSV is used for "Data-Driven" API testing (Scalability).
 * 2. SQL is used for "State Verification" (Accuracy).
 */
public class IamDataDrivenTest {

    // Database configuration
    private final String DB_URL = "jdbc:postgresql://localhost:5432/iam_db";
    private final String DB_USER = "tester";
    private final String DB_PASS = "password";

    /**
     * DataProvider reads the CSV file and returns an Iterator of objects.
     * This allows TestNG to run the @Test method once for every row in the CSV.
     */
    @DataProvider(name = "iamUserProvider")
    public Iterator<Object[]> getIamDataFromCsv() throws IOException, CsvException {
        List<Object[]> data = new ArrayList<>();
        String csvPath = "src/test/resources/iam_test_data.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            // Skip header row
            reader.skip(1);
            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                data.add(new Object[]{ row[0], row[1], row[2] }); // username, role, expectedStatus
            }
        }
        return data.iterator();
    }

    @Test(dataProvider = "iamUserProvider")
    public void testCreateUserApi(String username, String role, String expectedStatus) {
        // 1. API CALL (Action)
        // We use the data from the CSV to build the JSON request
        String requestBody = String.format(
                "{\"username\": \"%s\", \"role\": \"%s\"}",
                username, role
        );

        System.out.println("Testing IAM Creation for: " + username);

        RestAssured.given()
                .baseUri("https://api.example.com/v1")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/users")
                .then()
                .statusCode(Integer.parseInt(expectedStatus));

        // 2. SQL VERIFICATION (Verification)
        // After the API says "Success", we check the DB to ensure the data is actually there
        verifyUserInDatabase(username, role);
    }

    /**
     * SQL Method to verify the IAM state directly in the database.
     */
    private void verifyUserInDatabase(String username, String expectedRole) {
        String query = "SELECT role FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                String actualRole = rs.getString("role");
                assert actualRole.equals(expectedRole) : "DB Verification Failed!";
                System.out.println("SQL Verification Passed for " + username);
            } else {
                throw new RuntimeException("User not found in DB: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}