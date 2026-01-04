package com.icy.api;

import com.icy.api.utility.ConfigReader;
import com.icy.api.utility.LogManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import static org.hamcrest.Matchers.lessThan;

/**
 * BaseTest is the parent class for all your API tests.
 * It initializes the configurations, logging, and RestAssured Specifications.
 */
public class BaseTest {

    protected static LogManager logManager;
    protected static ConfigReader configReader;

    // requestSpec: Stores the Base URL and Headers
    protected static RequestSpecification requestSpec;

    // responseSpec: Stores common checks like Status 200 and Content-Type
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    public static void setupBase() {
        // Determine the environment (defaults to 'qa' if not specified in Maven)
        String envName = System.getProperty("env", "qa");

        try {
            // Initialize Utility Classes
            configReader = ConfigReader.getInstance();
            logManager = new LogManager(BaseTest.class);

            // 1. Configure the Request Specification
            // This prevents you from typing the URL and Content-Type in every single test.
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(configReader.getProperty("base.url"))
                    .setContentType(ContentType.JSON)
                    .addHeader("Accept", "application/json")
                    .build();

            // 2. Configure the Response Specification
            // This automatically validates that every API call returns 200 OK and is fast.
            responseSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectContentType(ContentType.JSON)
                    .expectResponseTime(lessThan(5000L))
                    .build();

            logManager.info("--- Base setup complete for Environment: " + envName + " ---");

        } catch (Exception e) {
            System.err.println("FATAL ERROR: Could not initialize BaseTest.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}