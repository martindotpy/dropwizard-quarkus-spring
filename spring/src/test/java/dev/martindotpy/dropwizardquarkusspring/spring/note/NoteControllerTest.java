package dev.martindotpy.dropwizardquarkusspring.spring.note;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.martindotpy.dropwizardquarkusspring.spring.core.SpringIntegrationTestSupport;
import io.restassured.http.ContentType;

class NoteControllerTest extends SpringIntegrationTestSupport {
    @BeforeEach
    void setUp() {
        setupRestAssured();
    }

    @Test
    void testCrudEndpoints() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(Map.of("content", "  nota spring  "))
                .when().post("/api/spring/note")
                .then()
                .statusCode(200)
                .body("data.content", is("nota spring"))
                .extract()
                .path("data.id");

        given()
                .when().get("/api/spring/note")
                .then()
                .statusCode(200)
                .body("data.find { it.id == '%s' }.content".formatted(id), is("nota spring"));

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("id", id, "content", "  nota actualizada spring  "))
                .when().put("/api/spring/note")
                .then()
                .statusCode(200)
                .body("data.id", is(id))
                .body("data.content", is("nota actualizada spring"));

        given()
                .when().delete("/api/spring/note/{id}", id)
                .then()
                .statusCode(200)
                .body("detail", containsString("Nota eliminada exitosamente"));
    }
}
