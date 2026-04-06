package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

public record ServiceResourceSnapshot(
        int availableProcessors,
        long maxMemoryBytes,
        long totalMemoryBytes,
        long freeMemoryBytes,
        long usedMemoryBytes,
        long usedMemoryMiB,
        long nonHeapUsedMemoryBytes,
        long nonHeapCommittedMemoryBytes,
        long nonHeapMaxMemoryBytes,
        long gcCollectionCount,
        long gcCollectionTimeMs,
        int liveThreadCount,
        int daemonThreadCount,
        int peakThreadCount,
        double processCpuLoad,
        double systemCpuLoad,
        long processCpuTimeNs,
        long processRssBytes,
        String javaVersion,
        String vmName,
        String osName,
        String osArch) {
}
