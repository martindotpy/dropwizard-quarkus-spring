package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Tag(name = "Miscellaneous", description = "Endpoints for miscellaneous information and testing.")
public class MiscellaneousController {
    public static String hostname;

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/hello")
    @Operation(hidden = true)
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/api/quarkus/hostname")
    @Produces(MediaType.TEXT_PLAIN)
    public String hostname() {
        return "Hello, my hostname is " + hostname;
    }
}
