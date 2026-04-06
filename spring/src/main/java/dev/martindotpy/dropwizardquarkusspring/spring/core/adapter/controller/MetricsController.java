package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrImageCatalog;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestListResponse;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestSummaryFactory;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrTokenResponse;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ImageManifestSummary;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceComparison;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceLiveMetrics;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceSnapshotFactory;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.client.GhcrManifestHttpClient;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.client.GhcrTokenHttpClient;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.configuration.StartupReadyTracker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/spring/cloud")
@RequiredArgsConstructor
@Tag(name = "Cloud", description = "Endpoints for cloud performance comparison.")
public class MetricsController {
    private static final long STREAM_INTERVAL_MS = 1000L;
    private static final Duration STREAM_INTERVAL = Duration.ofMillis(STREAM_INTERVAL_MS);

    private final GhcrTokenHttpClient ghcrTokenHttpClient;
    private final GhcrManifestHttpClient ghcrManifestHttpClient;
    private final StartupReadyTracker startupReadyTracker;

    private static final String HOSTNAME = resolveHostname();

    @Value("${spring.application.name:Spring API}")
    private String serviceName;

    @Value("${spring.application.version:1.0.0-SNAPSHOT}")
    private String serviceVersion;

    @GetMapping("/metrics/info")
    @Operation(summary = "Cloud static metrics", description = "Returns static metrics and image metadata used for framework comparison.")
    @ApiResponse(responseCode = "200", description = "Cloud static metrics retrieved successfully")
    public ServiceComparison staticMetrics() {
        return new ServiceComparison(
                "spring",
                serviceName,
                ServiceSnapshotFactory.RUNTIME_MODE,
                serviceVersion,
                HOSTNAME,
                startupReadyTracker.startupReadyMs(),
                fetchImageSummaries(GhcrImageCatalog.SPRING_IMAGE_NAMES));
    }

    @GetMapping(value = "/metrics/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Cloud live metrics stream", description = "Streams live startup and runtime metrics as server-sent events.")
    @ApiResponse(responseCode = "200", description = "Cloud live metrics stream started", content = @Content(schema = @Schema(implementation = ServiceLiveMetrics.class)))
    public Flux<ServerSentEvent<ServiceLiveMetrics>> liveMetricsStream() {
        return Flux.interval(STREAM_INTERVAL)
                .map(tick -> ServerSentEvent.<ServiceLiveMetrics>builder()
                        .event("metrics")
                        .data(ServiceSnapshotFactory.liveMetrics())
                        .build());
    }

    // Helpers
    private List<ImageManifestSummary> fetchImageSummaries(List<String> imageNames) {
        return imageNames.stream()
                .map(this::fetchImageSummary)
                .toList();
    }

    private ImageManifestSummary fetchImageSummary(String imageName) {
        try {
            GhcrTokenResponse tokenResponse = ghcrTokenHttpClient.fetchToken(
                    GhcrImageCatalog.scopeForPull(imageName),
                    GhcrImageCatalog.SERVICE);

            if (tokenResponse == null || tokenResponse.token() == null || tokenResponse.token().isBlank()) {
                throw new RuntimeException("Unable to obtain GHCR token.");
            }

            GhcrManifestListResponse manifestResponse = ghcrManifestHttpClient.fetchManifest(
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

    private static String resolveHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "unknown";
        }
    }
}
