package com.icy.api;

import com.icy.api.utility.ConfigReader;
import com.icy.api.utility.LogManager;
import org.junit.jupiter.api.BeforeAll;

/**
 * BaseTest serves as the foundation for all test classes.
 * It initializes necessary utilities (ConfigReader, LogManager) once per test run.
 * All specific test classes (e.g., UserTests, PolicyTests) will inherit from this class.
 */
public class BaseTest {

    // Static variables to hold initialized utilities, accessible by all test classes

    protected static LogManager logManager;
    protected static ConfigReader configReader;

    /**
     * Initializes configuration and logging utilities before any tests run.
     * The @BeforeAll annotation ensures this method runs once per test class lifetime.
     */
    @BeforeAll
    public static void setupBase() {
        // 1. Determine the target environment from system properties (e.g., -Denv=qa)
        String envName = System.getProperty("env", "qa"); // Default to 'qa' if 'env' property is not set
        String fileName = envName + ".properties"; //example "application-qa.properties" or "application-staging.properties"

        // 2. Initialize the ConfigReader, loading properties from the configuration file
        try {
            // 2. Initialize the ConfigReader using the instance-based constructor.
            // This loads the properties file specific to the chosen environment (e.g., "qa.properties").
            configReader = ConfigReader.getInstance();

            // 3. Initialize the LogManager
            logManager = new LogManager(BaseTest.class);

            logManager.info("--- Base setup complete. Configuration and Logging initialized. ---");

        } catch (Exception e) {
            System.err.println("FATAL: Failed to initialize base components (ConfigReader or LogManager).");
            e.printStackTrace();
            // In a real project, you might throw a RuntimeException here to halt the build.
        }
    }
}