package com.icy.api.tests.hooks;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class demonstrates the Data Lifecycle (The Test Hook).
 * It ensures that for every test case, the database is in the
 * correct state by running setup and teardown SQL scripts.
 */
public class IamLifecycleManager {

    // In a real scenario, this would be an instance of your DB utility class
    // private DbUtils dbUtils = new DbUtils();

    /**
     * @BeforeMethod: The Setup Hook
     * This runs BEFORE every individual @Test.
     * Use this to call your SQL scripts that "seed" or create the user.
     */
    @BeforeMethod
    public void setUpUserContext() {
        System.out.println("ACTION: Integrating Data Lifecycle - Setup");
        // Example call to your utility:
        // dbUtils.execute("src/test/resources/scripts/setup_user.sql");
        System.out.println("Status: setup_user.sql executed. User created in DB.");
    }

    /**
     * The actual Test Method.
     * This is where your Test Runner logic would be called.
     */
    @Test
    public void testUserAccessLevel() {
        System.out.println("RUNNING: Verifying user role and status...");
        // This is where you'd use your IamDataParser list
        // and perform the actual assertions.
    }

    /**
     * @AfterMethod: The Teardown Hook
     * This runs AFTER every individual @Test, regardless of whether it passed or failed.
     * This is critical for "Data Hygiene" to ensure the DB stays clean.
     */
    @AfterMethod
    public void tearDownUserContext() {
        System.out.println("ACTION: Integrating Data Lifecycle - Teardown");
        // Example call to your utility:
        // dbUtils.execute("src/test/resources/scripts/teardown_user.sql");
        System.out.println("Status: teardown_user.sql executed. User deleted from DB.");
    }
}
