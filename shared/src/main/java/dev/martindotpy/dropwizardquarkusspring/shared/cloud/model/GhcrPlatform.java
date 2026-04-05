package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GhcrPlatform(String architecture) {
}
