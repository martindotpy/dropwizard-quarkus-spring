package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import com.sun.management.OperatingSystemMXBean;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ServiceSnapshotFactory {
    private static final long NOT_AVAILABLE = -1L;
    private static final double CPU_NOT_AVAILABLE = -1D;

    private static final String PROC_STATUS_PATH = "/proc/self/status";
    private static final String PROC_RSS_PREFIX = "VmRSS:";

    private static final String CGROUP_V2_MEMORY_CURRENT_PATH = "/sys/fs/cgroup/memory.current";

    private static final String CGROUP_V1_MEMORY_USAGE_PATH = "/sys/fs/cgroup/memory/memory.usage_in_bytes";

    public static final String RUNTIME_MODE = runtimeMode();

    public static ServiceLiveMetrics liveMetrics() {
        return new ServiceLiveMetrics(
                Instant.now(),
                startupMetrics(),
                resourceSnapshot());
    }

    public static ServiceResourceSnapshot resourceSnapshot() {
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        MemoryUsage nonHeap = memoryMXBean.getNonHeapMemoryUsage();
        long nonHeapUsed = nonHeap.getUsed();
        long nonHeapCommitted = nonHeap.getCommitted();
        long nonHeapMax = nonHeap.getMax();

        long gcCollectionCount = totalGcCollectionCount();
        long gcCollectionTimeMs = totalGcCollectionTimeMs();

        int liveThreadCount = threadMXBean.getThreadCount();
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();

        CpuSnapshot cpuSnapshot = cpuSnapshot();
        long processRssBytes = processRssBytes();
        ContainerMemorySnapshot containerMemorySnapshot = containerMemorySnapshot();

        return new ServiceResourceSnapshot(
                runtime.availableProcessors(),
                maxMemory,
                totalMemory,
                freeMemory,
                usedMemory,
                toMiB(usedMemory),
                nonHeapUsed,
                nonHeapCommitted,
                nonHeapMax,
                gcCollectionCount,
                gcCollectionTimeMs,
                liveThreadCount,
                daemonThreadCount,
                peakThreadCount,
                cpuSnapshot.processCpuLoad(),
                cpuSnapshot.systemCpuLoad(),
                cpuSnapshot.processCpuTimeNs(),
                processRssBytes,
                containerMemorySnapshot.usedBytes(),
                containerMemorySnapshot.usedMiB(),
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

    private static long totalGcCollectionCount() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        long total = 0L;

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            long value = gcBean.getCollectionCount();
            if (value >= 0L) {
                total += value;
            }
        }

        return total;
    }

    private static long totalGcCollectionTimeMs() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        long total = 0L;

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            long value = gcBean.getCollectionTime();
            if (value >= 0L) {
                total += value;
            }
        }

        return total;
    }

    private static CpuSnapshot cpuSnapshot() {
        Object osBean = ManagementFactory.getOperatingSystemMXBean();
        if (!(osBean instanceof OperatingSystemMXBean sunOsBean)) {
            return new CpuSnapshot(CPU_NOT_AVAILABLE, CPU_NOT_AVAILABLE, NOT_AVAILABLE);
        }

        double processCpuLoad = safeCpuLoad(sunOsBean.getProcessCpuLoad());
        double systemCpuLoad = safeCpuLoad(sunOsBean.getCpuLoad());
        long processCpuTimeNs = safeLongMetric(sunOsBean.getProcessCpuTime());

        return new CpuSnapshot(processCpuLoad, systemCpuLoad, processCpuTimeNs);
    }

    private static long processRssBytes() {
        Path statusPath = Path.of(PROC_STATUS_PATH);
        if (!Files.isReadable(statusPath)) {
            return NOT_AVAILABLE;
        }

        try (BufferedReader reader = Files.newBufferedReader(statusPath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(PROC_RSS_PREFIX)) {
                    continue;
                }

                String[] parts = line.trim().split("\\s+");
                if (parts.length < 2) {
                    return NOT_AVAILABLE;
                }

                long kbValue = Long.parseLong(parts[1]);
                return kbValue * 1024L;
            }

            return NOT_AVAILABLE;
        } catch (IOException | NumberFormatException ex) {
            return NOT_AVAILABLE;
        }
    }

    private static ContainerMemorySnapshot containerMemorySnapshot() {
        long usedBytes = firstReadableMetric(List.of(
                CGROUP_V2_MEMORY_CURRENT_PATH,
                CGROUP_V1_MEMORY_USAGE_PATH));

        long usedMiB = usedBytes >= 0L ? toMiB(usedBytes) : NOT_AVAILABLE;

        return new ContainerMemorySnapshot(usedBytes, usedMiB);
    }

    private static long firstReadableMetric(List<String> paths) {
        for (String path : paths) {
            long value = readLongMetric(path);
            if (value >= 0L) {
                return value;
            }
        }

        return NOT_AVAILABLE;
    }

    private static long readLongMetric(String path) {
        Path metricPath = Path.of(path);
        if (!Files.isReadable(metricPath)) {
            return NOT_AVAILABLE;
        }

        try {
            String rawValue = Files.readString(metricPath).trim();
            long parsedValue = Long.parseLong(rawValue);

            return parsedValue >= 0L ? parsedValue : NOT_AVAILABLE;
        } catch (IOException | NumberFormatException ex) {
            return NOT_AVAILABLE;
        }
    }

    private static double safeCpuLoad(double value) {
        return value >= 0D ? value : CPU_NOT_AVAILABLE;
    }

    private static long safeLongMetric(long value) {
        return value >= 0L ? value : NOT_AVAILABLE;
    }

    private static long toMiB(long bytes) {
        return bytes / (1024L * 1024L);
    }

    private record CpuSnapshot(double processCpuLoad, double systemCpuLoad, long processCpuTimeNs) {
    }

    private record ContainerMemorySnapshot(long usedBytes, long usedMiB) {
    }
}
