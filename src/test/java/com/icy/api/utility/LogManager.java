package com.icy.api.utility;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Manages the application's logging configuration, reading settings
 * from the ConfigReader instance.
 * * NOTE: This implementation correctly assumes ConfigReader is now a Singleton
 * and accesses it using ConfigReader.getInstance().
 */
public class LogManager {

    // Define the main application logger
    private static final Logger APP_LOGGER = Logger.getLogger("AppLogger");

    // Default values in case configuration is missing
    private static final String DEFAULT_LOG_FILE = "app.log";
    private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

    // Flag to ensure static configuration runs only once
    private static volatile boolean configured = false;

    // Instance field to hold the name of the class using this LogManager instance
    private final String callingClassName;
    /**
     * FIX: Adds the required constructor invoked from BaseTest.java.
     * This makes LogManager usable as an instance in BaseTest.
     * * @param clazz The class (e.g., BaseTest.class) that is creating this logger instance.
     */
    public LogManager(Class<?> clazz) {
        this.callingClassName = clazz.getSimpleName();
        // Ensure the static logger configuration runs when the first instance is created
        if (!configured) {
            configureLogger();
            configured = true;
        }

    }

    /**
     * Logs an informational message tagged with the calling class name.
     * This is the instance method that will be used by BaseTest (e.g., logManager.info("...")).
     * * @param message The message to log.
     */
    public void info(String message) {
        APP_LOGGER.log(Level.INFO, "[{0}] {1}", new Object[]{callingClassName, message});
    }

    /**
     * Logs an error message tagged with the calling class name.
     * This is the instance method that will be used by BaseTest.
     * * @param message The error message to log.
     */
    public void error(String message) {
        APP_LOGGER.log(Level.SEVERE, "[{0}] {1}", new Object[]{callingClassName, message});
    }

    /**
     * Configures the logging system by reading properties from the single
     * instance of ConfigReader. This method is typically called once at
     * application startup.
     */
    public static void configureLogger() {
        // --- FIX HERE: Use the static getInstance() method instead of the private constructor ---
        ConfigReader configReader = ConfigReader.getInstance();

        // Read configuration values using instance methods (getProperty, etc.)
        String logFilePath = configReader.getProperty("log.file.path");
        String logLevelName = configReader.getProperty("log.level");

        // --- 1. Set the Log Level ---
        Level logLevel = DEFAULT_LOG_LEVEL;
        if (logLevelName != null) {
            try {
                // Convert string to Level object
                logLevel = Level.parse(logLevelName.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Log a warning using the default logger setup, then proceed with default level
                APP_LOGGER.log(Level.WARNING, "Invalid log level configured: " + logLevelName + ". Using default: " + DEFAULT_LOG_LEVEL);
            }
        }

        // Set the level for the application logger
        APP_LOGGER.setLevel(logLevel);

        // --- 2. Set up the FileHandler (to write to a file) ---
        try {
            // Use the configured path or default
            String finalLogPath = (logFilePath != null) ? logFilePath : DEFAULT_LOG_FILE;

            // Create a file handler: will write to file, 'true' means append mode
            FileHandler fileHandler = new FileHandler(finalLogPath, true);
            fileHandler.setFormatter(new SimpleFormatter());

            // Attach the new handler to the logger, ensuring we don't add duplicates
            boolean handlerExists = false;
            for (java.util.logging.Handler handler : APP_LOGGER.getHandlers()) {
                if (handler instanceof FileHandler) {
                    handlerExists = true;
                    break;
                }
            }

            if (!handlerExists) {
                APP_LOGGER.addHandler(fileHandler);
            }

            APP_LOGGER.log(Level.INFO, "Logger configured successfully. Writing logs to: " + finalLogPath + " at level: " + APP_LOGGER.getLevel().getName());

        } catch (IOException e) {
            APP_LOGGER.log(Level.SEVERE, "Failed to initialize FileHandler for logging.", e);
        }
    }

    /**
     * Gets the application logger for external use.
     * @return The configured application logger.
     */
    public static Logger getLogger() {
        return APP_LOGGER;
    }
}