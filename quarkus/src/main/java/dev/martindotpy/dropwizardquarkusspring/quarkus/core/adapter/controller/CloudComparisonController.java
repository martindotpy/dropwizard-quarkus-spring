package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.controller;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.server.jaxrs.OutboundSseEventImpl;

import dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.client.GhcrManifestClient;
import dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.client.GhcrTokenClient;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrImageCatalog;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestListResponse;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestSummaryFactory;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrTokenResponse;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ImageManifestSummary;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceComparasion;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceLiveMetrics;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceResourceSnapshot;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceSnapshotFactory;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceStartupMetrics;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;

@RegisterForReflection(targets = {
        ServiceLiveMetrics.class,
        ServiceStartupMetrics.class,
        ServiceResourceSnapshot.class
})
@Path("/api/quarkus/cloud")
@Tag(name = "Cloud", description = "Endpoints for cloud performance comparison.")
public class CloudComparisonController {
    private static final Logger LOGGER = Logger.getLogger(CloudComparisonController.class);
    private static final Duration STREAM_INTERVAL = Duration.ofSeconds(1);
    private static final String BOOTSTRAP_TIMING_CLASS = "io.quarkus.bootstrap.runner.Timing";
    private static final String MAIN_TIMING_FIELD = "main";
    private static final String BOOT_START_TIME_FIELD = "bootStartTime";

    private static final String HOSTNAME = hostname();

    @Inject
    @RestClient
    private GhcrTokenClient ghcrTokenClient;

    @Inject
    @RestClient
    private GhcrManifestClient ghcrManifestClient;

    private final long frameworkStartedAtMs = System.currentTimeMillis();
    private volatile long startupReadyMs;

    @ConfigProperty(name = "quarkus.application.name", defaultValue = "Quarkus")
    String serviceName;

    @ConfigProperty(name = "quarkus.application.version", defaultValue = "1.0.0-SNAPSHOT")
    String serviceVersion;

    @GET
    @Path("/metrics/info")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cloud static metrics", description = "Returns static metrics and image metadata used for framework comparison.")
    @APIResponse(responseCode = "200", description = "Cloud static metrics retrieved successfully")
    public ServiceComparasion staticMetrics() {
        return new ServiceComparasion(
                "quarkus",
                serviceName,
                ServiceSnapshotFactory.RUNTIME_MODE,
                serviceVersion,
                HOSTNAME,
                startupReadyMs,
                fetchImageSummaries(GhcrImageCatalog.QUARKUS_IMAGE_NAMES));
    }

    void onStartup(@Observes StartupEvent event) {
        startupReadyMs = resolveQuarkusStartupReadyMs();
    }

    @GET
    @Path("/metrics/live")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cloud live metrics stream", description = "Streams live startup and runtime metrics as server-sent events.")
    @APIResponse(responseCode = "200", description = "Cloud live metrics stream started")
    public Multi<OutboundSseEvent> liveMetricsStream() {
        return Multi.createFrom().ticks().every(STREAM_INTERVAL)
                .onItem().transform(tick -> toSseEvent(buildLiveMetrics()));
    }

    // Helpers
    private ServiceLiveMetrics buildLiveMetrics() {
        return ServiceSnapshotFactory.liveMetrics();
    }

    private List<ImageManifestSummary> fetchImageSummaries(List<String> imageNames) {
        return imageNames.stream()
                .map(this::fetchImageSummary)
                .toList();
    }

    private static OutboundSseEvent toSseEvent(ServiceLiveMetrics payload) {
        return new OutboundSseEventImpl.BuilderImpl()
                .name("metrics")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(ServiceLiveMetrics.class, payload)
                .build();
    }

    private long resolveQuarkusStartupReadyMs() {
        // Quarkus startup log time is computed from
        // io.quarkus.bootstrap.runner.Timing.bootStartTime,
        // so we use the same source to keep parity with the reported startup seconds.
        try {
            Class<?> timingClass = Class.forName(BOOTSTRAP_TIMING_CLASS);
            Field mainField = timingClass.getDeclaredField(MAIN_TIMING_FIELD);
            mainField.setAccessible(true);

            Object mainTiming = mainField.get(null);
            if (mainTiming == null) {
                return fallbackStartupReadyMs();
            }

            Field bootStartField = timingClass.getDeclaredField(BOOT_START_TIME_FIELD);
            bootStartField.setAccessible(true);

            long bootStartTime = bootStartField.getLong(mainTiming);
            if (bootStartTime <= 0) {
                return fallbackStartupReadyMs();
            }

            long elapsedNanos = System.nanoTime() - bootStartTime;
            if (elapsedNanos <= 0) {
                return fallbackStartupReadyMs();
            }

            return TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        } catch (Exception ex) {
            LOGGER.warn("Unable to resolve Quarkus startup timing from bootstrap runtime.", ex);
            return fallbackStartupReadyMs();
        }
    }

    private long fallbackStartupReadyMs() {
        long value = System.currentTimeMillis() - frameworkStartedAtMs;
        return value >= 0 ? value : 0L;
    }

    private ImageManifestSummary fetchImageSummary(String imageName) {
        try {
            GhcrTokenResponse tokenResponse = ghcrTokenClient.fetchToken(
                    GhcrImageCatalog.scopeForPull(imageName),
                    GhcrImageCatalog.SERVICE);

            if (tokenResponse == null || tokenResponse.token() == null || tokenResponse.token().isBlank()) {
                throw new RuntimeException("Unable to obtain GHCR token.");
            }

            GhcrManifestListResponse manifestResponse = ghcrManifestClient.fetchManifest(
                    GhcrImageCatalog.OWNER,
                    GhcrImageCatalog.REPOSITORY,
                    imageName,
                    "Bearer " + tokenResponse.token(),
                    GhcrImageCatalog.MANIFEST_ACCEPT_HEADER);

            if (manifestResponse == null) {
                throw new RuntimeException("Unable to obtain manifest for image: " + imageName);
            }

            return GhcrManifestSummaryFactory.success(imageName, manifestResponse);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error fetching manifest summary for image: " + imageName, ex);
        }
    }

    private static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "unknown";
        }
    }
}
