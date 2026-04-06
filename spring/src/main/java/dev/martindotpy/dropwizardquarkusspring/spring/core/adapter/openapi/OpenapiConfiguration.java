package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.openapi;

import java.util.Collections;
import java.util.Objects;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenapiConfiguration {
    @Bean
    OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.setServers(Collections.emptyList());

            // Prefer the default hey-api operation id generator
            openApi.getPaths().values().stream()
                    .filter(Objects::nonNull)
                    .forEach(
                            pathItem -> pathItem.readOperations().forEach(operation -> operation.setOperationId(null)));
        };
    }
}
