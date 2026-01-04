package com.icy.api.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader updated to handle application.properties, application-qa.properties,
 * and application-staging.properties.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;
    private static final String BASE_PATH = "src/test/resources/";

    private ConfigReader() {
        properties = new Properties();
        loadAllProperties();
    }

    /**
     * Singleton instance provider.
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Loads base properties first, then overlays environment-specific properties.
     */
    private void loadAllProperties() {
        // 1. Load the default application.properties first
        loadFromFile(BASE_PATH + "application.properties");

        // 2. Identify the environment (default to 'qa' if not provided via -Denv)
        String env = System.getProperty("env", "qa").toLowerCase();

        // 3. Construct the specific filename (e.g., application-qa.properties)
        String envFileName = "application-" + env + ".properties";

        // 4. Load the env file (this will override any duplicate keys in base properties)
        loadFromFile(BASE_PATH + envFileName);
    }

    private void loadFromFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            System.out.println("Loaded properties from: " + filePath);
        } catch (IOException e) {
            // We only log a warning because the specific env file might be optional
            System.out.println("Warning: Could not find or load " + filePath);
        }
    }

    /**
     * Gets value by key from the loaded properties.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}