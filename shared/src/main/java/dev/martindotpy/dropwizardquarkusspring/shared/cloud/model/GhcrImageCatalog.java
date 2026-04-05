package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.util.List;

public final class GhcrImageCatalog {
    public static final String BASE_URL = "https://ghcr.io";
    public static final String SERVICE = "ghcr.io";
    public static final String OWNER = "martindotpy";
    public static final String REPOSITORY = "dropwizard-quarkus-spring";
    public static final String MANIFEST_ACCEPT_HEADER = "application/vnd.oci.image.index.v1+json, "
            + "application/vnd.docker.distribution.manifest.list.v2+json, "
            + "application/vnd.docker.distribution.manifest.v2+json";

    public static final List<String> IMAGE_NAMES = List.of(
            "quarkus-native",
            "quarkus",
            "dropwizard",
            "spring");

    public static final List<String> DROPWIZARD_IMAGE_NAMES = List.of("dropwizard");
    public static final List<String> QUARKUS_IMAGE_NAMES = List.of("quarkus-native", "quarkus");
    public static final List<String> SPRING_IMAGE_NAMES = List.of("spring");

    private GhcrImageCatalog() {
    }

    public static String scopeForPull(String imageName) {
        return "repository:" + OWNER + "/" + REPOSITORY + "/" + imageName + ":pull";
    }

    public static String imageReference(String imageName) {
        return "ghcr.io/" + OWNER + "/" + REPOSITORY + "/" + imageName + ":latest";
    }
}
