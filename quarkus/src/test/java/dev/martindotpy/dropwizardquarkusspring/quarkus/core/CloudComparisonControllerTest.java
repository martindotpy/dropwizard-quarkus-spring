package dev.martindotpy.dropwizardquarkusspring.quarkus.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class CloudComparisonControllerTest {
    @Test
    void testCloudStaticMetricsEndpoint() {
        given()
                .when().get("/api/quarkus/cloud/metrics/info")
                .then()
                .statusCode(200)
                .body("framework", is("quarkus"));
    }
}
