package com.dasb.examples;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(DataProviderRunner.class)
public class Example2 {

    private static RequestSpecification requestSpec;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9876);



    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                build();

    }

    /*******************************************************
     * Create a DataProvider with three test data rows:
     * country code - zip code - state
     * us           - 90210    - California
     * us           - 12345    - New York
     * ca           - Y1A      - Yukon
     ******************************************************/
    @DataProvider
    public static  Object[][] createZipCodeData() {
        return new Object[][] {
                { "us", "90210", "California" },
                { "us", "12345", "New York" },
                { "ca", "Y1A", "Yukon" }
        };
    }


    /*******************************************************
     * Request zip code data for given country / zip
     * combinations by sending a GET to /<countryCode>/<zipCode>.
     * Use the test data collection created
     * above. Check that the state returned by the API
     * matches the expected value.
     ******************************************************/


    @Test
    @UseDataProvider("createZipCodeData")
    public void checkStateForCountryCodeAndZipCode(String countryCode, String zipCode, String expectedState) {

        given().
                spec(requestSpec).
                and().
                pathParam("countryCode", countryCode).
                pathParam("zipCode", zipCode).
                when().
                get("/{countryCode}/{zipCode}").
                then().
                assertThat().
                body("places[0].state",equalTo(expectedState));
    }

}
