package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrTokenResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "ghcr-token-api")
@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public interface GhcrTokenClient {
    @GET
    GhcrTokenResponse fetchToken(@RestQuery String scope, @RestQuery String service);
}
