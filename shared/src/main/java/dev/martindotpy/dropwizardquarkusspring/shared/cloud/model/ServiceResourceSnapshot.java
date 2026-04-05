package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

public record ServiceResourceSnapshot(
        int availableProcessors,
        long maxMemoryBytes,
        long totalMemoryBytes,
        long freeMemoryBytes,
        long usedMemoryBytes,
        long usedMemoryMiB,
        String javaVersion,
        String vmName,
        String osName,
        String osArch) {
}
