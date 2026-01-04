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
 * BaseTest initializes configurations and RestAssured specifications.
 * It automatically adapts to 'qa', 'staging', or 'prod' based on the -Denv flag.
 */
public class BaseTest {

    protected static LogManager logManager;
    protected static ConfigReader configReader;
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    public static void setupBase() {
        try {
            // 1. Initialize Utility Classes
            // ConfigReader.getInstance() already handles loading the correct .properties file based on -Denv
            configReader = ConfigReader.getInstance();
            logManager = new LogManager(BaseTest.class);

            // 2. Fetch the environment name for logging purposes
            String currentEnv = System.getProperty("env", "qa").toUpperCase();

            // 3. Configure the Request Specification
            // base.url will now correctly return the staging URL if -Denv=staging is used
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(configReader.getProperty("base.url"))
                    .setContentType(ContentType.JSON)
                    .addHeader("Accept", "application/json")
                    // Optional: If staging/prod requires specific headers, you can fetch them here
                    // .addHeader("X-Environment", currentEnv)
                    .build();

            // 4. Configure the Response Specification
            responseSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectContentType(ContentType.JSON)
                    .expectResponseTime(lessThan(5000L))
                    .build();

            logManager.info("--- Test Suite Initialized ---");
            logManager.info("Active Environment: " + currentEnv);
            logManager.info("Base URL: " + configReader.getProperty("base.url"));

        } catch (Exception e) {
            System.err.println("FATAL ERROR: Could not initialize BaseTest specifications.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}