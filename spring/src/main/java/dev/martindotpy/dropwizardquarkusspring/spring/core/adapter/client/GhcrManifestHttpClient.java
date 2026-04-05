package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrManifestListResponse;

@HttpExchange
public interface GhcrManifestHttpClient {
    @GetExchange("/v2/{owner}/{repository}/{image}/manifests/latest")
    GhcrManifestListResponse fetchManifest(
            @PathVariable String owner,
            @PathVariable String repository,
            @PathVariable String image,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Accept") String accept);
}
