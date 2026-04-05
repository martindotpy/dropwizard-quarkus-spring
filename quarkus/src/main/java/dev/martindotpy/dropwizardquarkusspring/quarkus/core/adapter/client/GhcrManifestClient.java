package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestListResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "ghcr-manifest-api")
@Path("/v2/{owner}/{repository}/{image}/manifests")
@Produces(MediaType.APPLICATION_JSON)
public interface GhcrManifestClient {
    @GET
    @Path("/latest")
    GhcrManifestListResponse fetchManifest(
            @PathParam("owner") String owner,
            @PathParam("repository") String repository,
            @PathParam("image") String image,
            @HeaderParam("Authorization") String authorization,
            @HeaderParam("Accept") String accept);
}
