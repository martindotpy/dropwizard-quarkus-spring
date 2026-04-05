package dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrImageCatalog;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestListResponse;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestSummaryFactory;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrTokenResponse;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ImageManifestSummary;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceComparasion;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceLiveMetrics;
import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.ServiceSnapshotFactory;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import lombok.RequiredArgsConstructor;

@Path("/api/dropwizard/cloud")
@Tag(name = "Cloud", description = "Endpoints for cloud performance comparison.")
@RequiredArgsConstructor
public class CloudComparisonController {
    private static final long STREAM_INTERVAL_MS = 1000L;

    private final Client ghcrClient;
    private final String serviceName;
    private final String version;
    private final AtomicLong startupReadyMs;

    private static final String HOSTNAME = hostname();

    @GET
    @Path("/metrics/info")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cloud static metrics", description = "Returns static metrics and image metadata used for framework comparison.")
    @APIResponse(responseCode = "200", description = "Cloud static metrics retrieved successfully")
    public ServiceComparasion staticMetrics() {
        return new ServiceComparasion(
                "dropwizard",
                serviceName,
                ServiceSnapshotFactory.RUNTIME_MODE,
                version,
                HOSTNAME,
                startupReadyMs.get(),
                fetchImageSummaries(GhcrImageCatalog.DROPWIZARD_IMAGE_NAMES));
    }

    @GET
    @Path("/metrics/live")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Operation(summary = "Cloud live metrics stream", description = "Streams live startup and runtime metrics as server-sent events.")
    @APIResponse(responseCode = "200", description = "Cloud live metrics stream started", content = @Content(mediaType = MediaType.SERVER_SENT_EVENTS, schema = @Schema(implementation = ServiceLiveMetrics.class)))
    public void liveMetricsStream(@Context SseEventSink eventSink, @Context Sse sse) {
        Thread.ofVirtual().start(() -> streamMetrics(eventSink, sse));
    }

    // Helpers
    private ImageManifestSummary fetchImageSummary(String imageName) {
        try {
            GhcrTokenResponse tokenResponse = ghcrClient.target(GhcrImageCatalog.BASE_URL)
                    .path("token")
                    .queryParam("scope", GhcrImageCatalog.scopeForPull(imageName))
                    .queryParam("service", GhcrImageCatalog.SERVICE)
                    .request(MediaType.APPLICATION_JSON)
                    .get(GhcrTokenResponse.class);

            if (tokenResponse == null || tokenResponse.token() == null || tokenResponse.token().isBlank()) {
                throw new RuntimeException("Unable to obtain GHCR token.");
            }

            GhcrManifestListResponse manifestResponse = ghcrClient.target(GhcrImageCatalog.BASE_URL)
                    .path("v2")
                    .path(GhcrImageCatalog.OWNER)
                    .path(GhcrImageCatalog.REPOSITORY)
                    .path(imageName)
                    .path("manifests")
                    .path("latest")
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.ACCEPT, GhcrImageCatalog.MANIFEST_ACCEPT_HEADER)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.token())
                    .get(GhcrManifestListResponse.class);

            if (manifestResponse == null) {
                throw new RuntimeException("Unable to obtain manifest for image: " + imageName);
            }

            return GhcrManifestSummaryFactory.success(imageName, manifestResponse);
        } catch (WebApplicationException | ProcessingException ex) {
            throw new RuntimeException("Error fetching manifest summary for image: " + imageName, ex);
        }
    }

    private List<ImageManifestSummary> fetchImageSummaries(List<String> imageNames) {
        return imageNames.stream()
                .map(this::fetchImageSummary)
                .toList();
    }

    private void streamMetrics(SseEventSink eventSink, Sse sse) {
        try (eventSink) {
            while (!Thread.currentThread().isInterrupted() && !eventSink.isClosed()) {
                eventSink.send(toSseEvent(sse, ServiceSnapshotFactory.liveMetrics())).toCompletableFuture().join();
                Thread.sleep(STREAM_INTERVAL_MS);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException ex) {
            // Client disconnected; stop the stream loop
        }
    }

    private static OutboundSseEvent toSseEvent(Sse sse, ServiceLiveMetrics payload) {
        return sse.newEventBuilder()
                .name("metrics")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(ServiceLiveMetrics.class, payload)
                .build();
    }

    private static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "unknown";
        }
    }
}
