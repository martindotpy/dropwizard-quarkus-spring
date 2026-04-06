package dev.martindotpy.dropwizardquarkusspring.dropwizard.core;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.DropwizardApplication;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.DropwizardConfiguration;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(DropwizardExtensionsSupport.class)
public abstract class DropwizardIntegrationTestSupport {
    private static final String MONGO_IMAGE = "mongo:7";
    private static final String TEST_DATABASE = "notes-db-test";

    private static final MongoDBContainer MONGO = new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE));

    static {
        MONGO.start();
    }

    protected static final DropwizardAppExtension<DropwizardConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardApplication.class,
            ResourceHelpers.resourceFilePath("test-config.yaml"),
            ConfigOverride.config("mongo.uri", () -> MONGO.getReplicaSetUrl(TEST_DATABASE)),
            ConfigOverride.config("mongo.database", TEST_DATABASE));

    protected static String baseUrl() {
        return "http://localhost:" + APP.getLocalPort();
    }
}
