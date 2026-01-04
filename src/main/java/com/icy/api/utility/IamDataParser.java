package com.icy.api.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to parse IAM test data for API or UI validation.
 * Expected CSV Format: username,role,expectedStatus
 */
public class IamDataParser {

    /**
     * Model representing a single row in the iam.csv file.
     */
    public static class IamTestCase {
        private final String username;
        private final String role;
        private final int expectedStatus;

        public IamTestCase(String username, String role, int expectedStatus) {
            this.username = username;
            this.role = role;
            this.expectedStatus = expectedStatus;
        }

        public String getUsername() { return username; }
        public String getRole() { return role; }
        public int getExpectedStatus() { return expectedStatus; }

        @Override
        public String toString() {
            return String.format("Test Case [User: %-16s | Role: %-10s | Status: %d]",
                    username, role, expectedStatus);
        }
    }

    /**
     * Reads the CSV and returns a list of IamTestCase objects.
     * @param filePath The relative path to the csv file.
     */
    public static List<IamTestCase> loadTestData(String filePath) {
        List<IamTestCase> testCases = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header row: username,role,expectedStatus
            String header = br.readLine();

            while ((line = br.readLine()) != null) {
                // Split by comma, ensuring we handle potential empty spaces
                String[] columns = line.split(",");

                if (columns.length >= 3) {
                    try {
                        String user = columns[0].trim();
                        String role = columns[1].trim();
                        int status = Integer.parseInt(columns[2].trim());

                        testCases.add(new IamTestCase(user, role, status));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping row due to invalid status code: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read file at: " + filePath);
            e.printStackTrace();
        }

        return testCases;
    }

    public static void main(String[] args) {
        // Example execution to verify data loading
        String csvPath = "src/test/resources/data/iam.csv";
        List<IamTestCase> data = loadTestData(csvPath);

        System.out.println("Successfully loaded " + data.size() + " test scenarios:");
        data.forEach(System.out::println);
    }
}
