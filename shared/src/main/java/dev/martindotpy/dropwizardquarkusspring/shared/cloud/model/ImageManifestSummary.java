package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.util.List;

public record ImageManifestSummary(
        String image,
        String imageReference,
        List<ImagePlatformDigest> platforms) {
}
