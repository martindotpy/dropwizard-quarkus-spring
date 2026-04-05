package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GhcrManifestListResponse(List<GhcrManifestItem> manifests) {
}
