package com.bluespurs.starterkit.controller;

import com.bluespurs.starterkit.IntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@Category(IntegrationTest.class)
public class HelloWorldControllerIntegrationTest extends IntegrationTest {
    /**
     * A simple test to make sure the home page responds with the correct information.
     *
     * @see HelloWorldController#helloWorld()
     */
    @Test
    public void testHelloWorld() {
        when()
                .get("/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(HelloWorldController.INTRO));
    }
}
