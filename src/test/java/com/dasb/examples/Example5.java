package com.dasb.examples;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Example5 {

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


    @Test
    public void fromUserId_findPhotoTitle_expectPariaturSuntEveniet() {
        /*******************************************************
         * Perform a GET to /users and extract the user id
         * that corresponds to the user with username 'Karianne'
         *
         * Hint: use extract().path() and a 'find' filter to do this.
         *
         * Store the user id in a variable of type int
         ******************************************************/

        int userId = given().
                spec(requestSpec).
                when().
                get("/users").
                then().extract().
                path("find{it.username == 'Karianne'}id");

        /*******************************************************
         * Use a JUnit assertEquals to verify that the userId
         * is equal to 4
         ******************************************************/

        Assert.assertEquals(4, userId);

        /*******************************************************
         * Perform a GET to /albums and extract all albums that
         * are associated with the previously retrieved user id.
         *
         * Hint: use extract().path() and a 'findAll' to do this.
         *
         * Store these in a variable of type List<Integer>.
         ******************************************************/
       List<Integer> albumIds =
        given().
                spec(requestSpec).
          when().
                get("/albums").
          then().extract().
                path(String.format("findAll{it.userId==%d}.id", userId));

        /*******************************************************
         * Use a JUnit assertEquals to verify that the list has
         * exactly 10 items (hint: use the size() method)
         ******************************************************/

        Assert.assertEquals(10, albumIds.size());


    }
}
