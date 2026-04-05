package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import dev.martindotpy.dropwizardquarkusspring.shared.cloud.model.GhcrImageCatalog;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.client.GhcrManifestHttpClient;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.client.GhcrTokenHttpClient;

@Configuration
public class GhcrClientConfiguration {
    @Bean
    GhcrTokenHttpClient ghcrTokenHttpClient(RestClient.Builder ghcrRestClientBuilder) {
        RestClient restClient = ghcrRestClientBuilder
                .baseUrl(GhcrImageCatalog.BASE_URL)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(GhcrTokenHttpClient.class);
    }

    @Bean
    GhcrManifestHttpClient ghcrManifestHttpClient(RestClient.Builder ghcrRestClientBuilder) {
        RestClient restClient = ghcrRestClientBuilder
                .baseUrl(GhcrImageCatalog.BASE_URL)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(GhcrManifestHttpClient.class);
    }
}
