package com.icy.api.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DbUtils: Updated to handle SQL file execution for the Test Hook lifecycle.
 */
public class DbUtils {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db";
    private static final String USER = "tester";
    private static final String PASS = "password123";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return connection;
    }

    /**
     * NEW METHOD: Bridge between your .sql files and the database.
     * This allows Lesson 8 "Test Hooks" to call files instead of hardcoded strings.
     */
    public static void executeSqlFile(String filePath) {
        try {
            // Read the entire SQL file into a string
            String query = new String(Files.readAllBytes(Paths.get(filePath)));
            executeUpdate(query);
        } catch (IOException e) {
            System.err.println("Could not read SQL file at: " + filePath);
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("DB Update Successful.");
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
        }
    }

    /**
     * Executes a SELECT query to verify API changes in the DB
     */
    public static void executeQuery(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Example: Print column 1
                System.out.println("Result: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

    /**
     * Closes the connection completely
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
