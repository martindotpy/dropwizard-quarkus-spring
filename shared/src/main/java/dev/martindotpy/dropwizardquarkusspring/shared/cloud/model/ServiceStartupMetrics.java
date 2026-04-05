package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.time.Instant;

public record ServiceStartupMetrics(
        Instant processStartedAt,
        long processUptimeMs,
        long processUptimeSeconds) {
}
