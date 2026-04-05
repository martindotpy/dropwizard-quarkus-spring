package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

public record ImagePlatformDigest(
        String architecture,
        String digest,
        long size) {
}
