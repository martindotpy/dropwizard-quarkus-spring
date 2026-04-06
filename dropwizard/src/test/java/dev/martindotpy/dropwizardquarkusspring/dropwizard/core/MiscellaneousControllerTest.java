package dev.martindotpy.dropwizardquarkusspring.dropwizard.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

class MiscellaneousControllerTest extends DropwizardIntegrationTestSupport {
    @Test
    void testHelloEndpoint() {
        given()
                .baseUri(baseUrl())
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Dropwizard REST"));
    }

    @Test
    void testHostnameEndpoint() {
        given()
                .baseUri(baseUrl())
                .when().get("/api/dropwizard/hostname")
                .then()
                .statusCode(200)
                .body(containsString("Hello, my hostname is"));
    }
}
