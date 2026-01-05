package com.icy.api.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {

    // 1. The DataProvider goes here, above the data-supplying method
    // Setting parallel = false ensures tests run one-by-one to avoid bot detection
    @DataProvider(name = "userStatusProvider", parallel = false)
    public Object[][] userStatusData() {
        return new Object[][] {
                { "valid@email.com", "password123", 200, "Success" },
                { "unverified@email.com", "password123", 403, "Email verification required" },
                { "blocked@email.com", "password123", 401, "Account blocked" }
        };
    }

    // 2. Your Test method points to the provider by name
    @Test(dataProvider = "userStatusProvider")
    public void testUserLoginStatus(String email, String password, int code, String msg) {
        // Your RestAssured logic here...
    }
}