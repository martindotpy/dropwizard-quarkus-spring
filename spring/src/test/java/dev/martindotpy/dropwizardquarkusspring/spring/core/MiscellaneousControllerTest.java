package dev.martindotpy.dropwizardquarkusspring.spring.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MiscellaneousControllerTest extends SpringIntegrationTestSupport {
    @BeforeEach
    void setUp() {
        setupRestAssured();
    }

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Spring REST"));
    }

    @Test
    void testHostnameEndpoint() {
        given()
                .when().get("/api/spring/hostname")
                .then()
                .statusCode(200)
                .body(containsString("Hello, my hostname is"));
    }
}
