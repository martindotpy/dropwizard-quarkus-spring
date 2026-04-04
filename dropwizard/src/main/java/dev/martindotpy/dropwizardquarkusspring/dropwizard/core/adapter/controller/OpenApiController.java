package dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.microprofile.openapi.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class OpenApiController {
    @GET
    @Path("/api/dropwizard/openapi.json")
    @Operation(hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    public String openapi() {
        try (InputStream stream = getClass().getResourceAsStream("/openapi/openapi.json")) {
            if (stream == null) {
                throw new InternalServerErrorException("OpenAPI schema not found. Run Maven build first.");
            }

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new InternalServerErrorException("Failed to read OpenAPI schema.", ex);
        }
    }
}
