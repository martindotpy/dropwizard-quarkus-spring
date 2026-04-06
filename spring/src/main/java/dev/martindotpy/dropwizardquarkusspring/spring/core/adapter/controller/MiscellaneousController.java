package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/")
@Tag(name = "Miscellaneous", description = "Endpoints for miscellaneous information and testing.")
public class MiscellaneousController {
    private static final String HOSTNAME = resolveHostname();

    @GetMapping("/hello")
    @Operation(hidden = true)
    public String hello() {
        return "Hello from Spring REST";
    }

    @GetMapping("/api/spring/hostname")
    @Operation(summary = "Hostname", description = "Returns the hostname of the server.")
    public String hostname() {
        return "Hello, my hostname is " + HOSTNAME;
    }

    private static String resolveHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "unknown";
        }
    }
}
