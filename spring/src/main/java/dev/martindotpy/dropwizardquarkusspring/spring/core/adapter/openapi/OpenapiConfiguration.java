package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.openapi;

import java.util.Collections;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenapiConfiguration {
    @Bean
    OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.setServers(Collections.emptyList());
        };
    }
}
