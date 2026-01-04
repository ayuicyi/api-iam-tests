package com.icy.api.utility;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Utility class to manage RestAssured Request and Response Specifications.
 */
public class BaseSpecManager {

    // Base URI could also be loaded from a properties file
    private static final String BASE_URI = "https://api.company.com/iam";

    /**
     * Returns a RequestSpecification for IAM-related API calls.
     * Must be static to be accessed as BaseSpecManager.getIAMRequestSpec()
     * * @return RequestSpecification
     */
    public static RequestSpecification getIAMRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                // Add common authentication headers here if needed
                // .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}