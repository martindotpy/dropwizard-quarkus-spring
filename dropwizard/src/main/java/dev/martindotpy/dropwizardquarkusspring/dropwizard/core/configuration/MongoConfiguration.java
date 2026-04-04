package dev.martindotpy.dropwizardquarkusspring.dropwizard.core.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoConfiguration {
    @NotBlank
    private String uri;

    @NotBlank
    private String database;
}
