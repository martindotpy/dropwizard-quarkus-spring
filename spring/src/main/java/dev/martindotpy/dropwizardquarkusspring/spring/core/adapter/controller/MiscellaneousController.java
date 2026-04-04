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
    public static String hostname;

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/hello")
    @Operation(hidden = true)
    public String hello() {
        return "Hello from Spring REST";
    }

    @GetMapping("/api/spring/hostname")
    public String hostname() {
        return "Hola desde Spring en " + hostname;
    }
}
