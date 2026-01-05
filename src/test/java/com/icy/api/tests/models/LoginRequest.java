package com.icy.api.tests.models;

/**
 * POJO for Login Request payload.
 * Using objects instead of Strings makes tests easier to maintain.
 */
public class LoginRequest {
    private String email;
    private String password;

    // Constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters (Required for Jackson/Gson to serialize)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
