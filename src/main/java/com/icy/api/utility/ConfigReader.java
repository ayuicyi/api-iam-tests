package com.icy.api.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Enhanced ConfigReader supporting multiple environments (qa, staging, prod).
 * Priority: System Properties > Env Properties > Base Properties.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;
    private static final String BASE_PATH = "src/test/resources/";

    private ConfigReader() {
        properties = new Properties();
        loadHierarchicalProperties();
    }

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

    private void loadHierarchicalProperties() {
        // 1. Load the common base properties
        loadFromFile(BASE_PATH + "application.properties");

        // 2. Determine environment (default to 'qa' if not provided)
        // Usage: mvn test -Denv=staging
        String env = System.getProperty("env", "qa").toLowerCase();

        // 3. Load Environment-Specific properties (application-staging.properties, etc.)
        String envFilePath = BASE_PATH + "application-" + env + ".properties";
        loadFromFile(envFilePath);

        // 4. Final Override with System properties (for Jenkins/CI/CD secret injection)
        properties.putAll(System.getProperties());
    }

    private void loadFromFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            System.out.println("[CONFIG] Successfully loaded: " + filePath);
        } catch (IOException e) {
            System.out.println("[CONFIG] Note: Optional config file not found: " + filePath);
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        return (value != null) ? value.trim() : null;
    }
}