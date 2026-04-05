package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.time.Instant;

public record ServiceLiveMetrics(
        Instant collectedAt,
        ServiceStartupMetrics startupMetrics,
        ServiceResourceSnapshot resourceSnapshot) {
}
