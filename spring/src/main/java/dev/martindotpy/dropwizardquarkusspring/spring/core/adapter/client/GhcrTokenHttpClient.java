package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrTokenResponse;

@HttpExchange
public interface GhcrTokenHttpClient {
    @GetExchange("/token")
    GhcrTokenResponse fetchToken(@RequestParam String scope, @RequestParam String service);
}
