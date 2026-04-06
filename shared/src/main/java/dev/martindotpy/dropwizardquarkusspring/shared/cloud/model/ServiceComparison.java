package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.util.List;

public record ServiceComparison(
        String framework,
        String service,
        String runtimeMode,
        String version,
        String hostname,
        long startupReadyMs,
        List<ImageManifestSummary> images) {
}
