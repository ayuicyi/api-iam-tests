package com.icy.api.tests.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * POJO for mapping iamREAL.csv data.
 * expectedStatus is set as an int to match numeric status codes (e.g., 200, 401).
 */
@JsonPropertyOrder({
        "username",
        "role",
        "expectedStatus"
})
public class UserTestData {

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
    private String role;

    @JsonProperty("expectedStatus")
    private int expectedStatus;

    // Default constructor required for Jackson/CsvMapper
    public UserTestData() {}

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getExpectedStatus() {
        return expectedStatus;
    }

    public void setExpectedStatus(int expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    @Override
    public String toString() {
        return "UserTestData{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", expectedStatus=" + expectedStatus +
                '}';
    }
}