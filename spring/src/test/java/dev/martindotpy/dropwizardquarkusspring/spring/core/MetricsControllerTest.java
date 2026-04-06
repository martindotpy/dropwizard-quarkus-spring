package dev.martindotpy.dropwizardquarkusspring.spring.core;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetricsControllerTest extends SpringIntegrationTestSupport {
    @BeforeEach
    void setUp() {
        setupRestAssured();
    }

    @Test
    void testCloudStaticMetricsEndpoint() {
        given()
                .when().get("/api/spring/cloud/metrics/info")
                .then()
                .statusCode(200)
                .body("framework", is("spring"))
                .body("startupReadyMs", greaterThanOrEqualTo(0));
    }

    @Test
    void testCloudMetricsLiveSseEndpoint() throws Exception {
        URI uri = URI.create("http://localhost:" + port + "/api/spring/cloud/metrics/live");
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "text/event-stream")
                .GET()
                .build();

        HttpResponse<InputStream> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofInputStream());

        try (InputStream _ = response.body()) {
            assertEquals(200, response.statusCode());

            String contentType = response.headers().firstValue("content-type").orElse("");
            assertTrue(contentType.contains("text/event-stream"));
        }
    }

    @Test
    void testOpenApiEndpoint() {
        given()
                .when().get("/api/spring/openapi.json")
                .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body(containsString("\"openapi\""));
    }
}
