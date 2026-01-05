package com.icy.api.utility;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.icy.api.tests.models.UserTestData;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to handle CSV data parsing for API testing.
 * Uses Jackson Dataformat CSV for high-performance POJO mapping.
 */
public class CSVUtils {

    /**
     * Reads a CSV file and converts it into a list of UserTestData objects.
     * * @param filePath The path to your iamREAL.csv file (e.g., "src/test/resources/iamREAL.csv")
     * @return A list of mapped UserTestData objects
     */
    public static List<UserTestData> readUserTestData(String filePath) {
        // Create a mapper that understands CSV format
        CsvMapper csvMapper = new CsvMapper();

        // Define the schema based on the POJO class, assuming the CSV has a header row
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("CSV file not found at: " + filePath);
            }

            // Map the file contents directly to the List of UserTestData
            MappingIterator<UserTestData> it = csvMapper
                    .readerFor(UserTestData.class)
                    .with(schema)
                    .readValues(file);

            return it.readAll();

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}