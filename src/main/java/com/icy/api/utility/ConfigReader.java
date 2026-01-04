package com.icy.api.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This is the "Senior" version.
 * It is robust (has error handling) but stays focused.
 */
public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            // First, load the main config to see which env we are in
            FileInputStream mainFile = new FileInputStream("src/main/resources/application.properties");
            properties.load(mainFile);

            String env = properties.getProperty("env", "qa"); // default to qa if not found

            // Then, load the environment specific file
            String envPath = "src/main/resources/application-" + env + ".properties";
            FileInputStream envFile = new FileInputStream(envPath);
            properties.load(envFile);

            System.out.println("Successfully loaded configuration for environment: " + env);
        } catch (IOException e) {
            System.err.println("FATAL: Could not load properties. Check your src/main/resources folder.");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}