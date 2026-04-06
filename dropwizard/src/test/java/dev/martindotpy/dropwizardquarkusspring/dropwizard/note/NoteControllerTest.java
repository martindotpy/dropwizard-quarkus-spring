package dev.martindotpy.dropwizardquarkusspring.dropwizard.note;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;

import org.junit.jupiter.api.Test;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.DropwizardIntegrationTestSupport;
import io.restassured.http.ContentType;

class NoteControllerTest extends DropwizardIntegrationTestSupport {
    @Test
    void testCrudEndpoints() {
        String id = given()
                .baseUri(baseUrl())
                .contentType(ContentType.JSON)
                .body(Map.of("content", "  nota dropwizard  "))
                .when().post("/api/dropwizard/note")
                .then()
                .statusCode(200)
                .body("data.content", is("nota dropwizard"))
                .extract()
                .path("data.id");

        given()
                .baseUri(baseUrl())
                .when().get("/api/dropwizard/note")
                .then()
                .statusCode(200)
                .body("data.find { it.id == '%s' }.content".formatted(id), is("nota dropwizard"));

        given()
                .baseUri(baseUrl())
                .contentType(ContentType.JSON)
                .body(Map.of("id", id, "content", "  nota actualizada dropwizard  "))
                .when().put("/api/dropwizard/note")
                .then()
                .statusCode(200)
                .body("data.id", is(id))
                .body("data.content", is("nota actualizada dropwizard"));

        given()
                .baseUri(baseUrl())
                .when().delete("/api/dropwizard/note/{id}", id)
                .then()
                .statusCode(200)
                .body("detail", containsString("Nota eliminada exitosamente"));
    }
}
