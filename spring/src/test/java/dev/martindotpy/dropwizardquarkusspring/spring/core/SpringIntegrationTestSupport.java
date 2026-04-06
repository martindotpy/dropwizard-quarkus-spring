package dev.martindotpy.dropwizardquarkusspring.spring.core;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public abstract class SpringIntegrationTestSupport {
    private static final String MONGO_IMAGE = "mongo:7";
    private static final String TEST_DATABASE = "notes-db-test";

    static final MongoDBContainer MONGO = new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE));

    static {
        MONGO.start();
    }

    @LocalServerPort
    int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", () -> MONGO.getReplicaSetUrl(TEST_DATABASE));
        registry.add("spring.data.mongodb.uri", () -> MONGO.getReplicaSetUrl(TEST_DATABASE));
        registry.add("spring.mongodb.database", () -> TEST_DATABASE);
        registry.add("spring.data.mongodb.database", () -> TEST_DATABASE);
    }

    protected void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
}
