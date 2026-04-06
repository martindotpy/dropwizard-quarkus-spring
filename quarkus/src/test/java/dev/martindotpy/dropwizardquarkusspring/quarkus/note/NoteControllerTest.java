package dev.martindotpy.dropwizardquarkusspring.quarkus.note;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class NoteControllerTest {
    @Test
    void testCrudEndpoints() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(Map.of("content", "  nota quarkus  "))
                .when().post("/api/quarkus/note")
                .then()
                .statusCode(200)
                .body("data.content", is("nota quarkus"))
                .extract()
                .path("data.id");

        given()
                .when().get("/api/quarkus/note")
                .then()
                .statusCode(200)
                .body("data.find { it.id == '%s' }.content".formatted(id), is("nota quarkus"));

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("id", id, "content", "  nota actualizada quarkus  "))
                .when().put("/api/quarkus/note")
                .then()
                .statusCode(200)
                .body("data.id", is(id))
                .body("data.content", is("nota actualizada quarkus"));

        given()
                .when().delete("/api/quarkus/note/{id}", id)
                .then()
                .statusCode(200)
                .body("detail", containsString("Nota eliminada exitosamente"));
    }
}
