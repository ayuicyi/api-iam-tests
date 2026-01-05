package com.icy.api.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for JSON operations.
 * Handles serialization/deserialization and deep path extraction.
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a Java Object into a JSON string.
     * Useful for logging or preparing request bodies.
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    /**
     * Reads a JSON file and maps it to a specific Java class.
     * Useful for loading static test data from files.
     */
    public static <T> T fromJsonFile(String filePath, Class<T> clazz) {
        try {
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Extracts a specific value from a Response using a GPath expression.
     * Example path: "user.profile.id"
     */
    public static String getFieldValue(Response response, String path) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getString(path);
    }
}
