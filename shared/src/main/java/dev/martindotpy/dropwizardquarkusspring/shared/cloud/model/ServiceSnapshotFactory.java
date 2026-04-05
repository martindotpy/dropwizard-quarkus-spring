package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.lang.management.ManagementFactory;
import java.time.Instant;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ServiceSnapshotFactory {
    public static final String RUNTIME_MODE = runtimeMode();

    public static ServiceLiveMetrics liveMetrics() {
        return new ServiceLiveMetrics(
                Instant.now(),
                startupMetrics(),
                resourceSnapshot());
    }

    public static ServiceResourceSnapshot resourceSnapshot() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        return new ServiceResourceSnapshot(
                runtime.availableProcessors(),
                maxMemory,
                totalMemory,
                freeMemory,
                usedMemory,
                toMiB(usedMemory),
                System.getProperty("java.version", "unknown"),
                System.getProperty("java.vm.name", "unknown"),
                System.getProperty("os.name", "unknown"),
                System.getProperty("os.arch", "unknown"));
    }

    public static ServiceStartupMetrics startupMetrics() {
        long startMillis = ManagementFactory.getRuntimeMXBean().getStartTime();
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();

        return new ServiceStartupMetrics(
                Instant.ofEpochMilli(startMillis),
                uptimeMs,
                uptimeMs / 1000L);
    }

    private static String runtimeMode() {
        return System.getProperty("org.graalvm.nativeimage.imagecode") != null ? "native" : "jvm";
    }

    private static long toMiB(long bytes) {
        return bytes / (1024L * 1024L);
    }
}
