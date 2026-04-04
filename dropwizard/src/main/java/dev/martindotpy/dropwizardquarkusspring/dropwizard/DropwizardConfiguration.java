package dev.martindotpy.dropwizardquarkusspring.dropwizard;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.configuration.MongoConfiguration;
import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropwizardConfiguration extends Configuration {
    @Valid
    @NotNull
    private MongoConfiguration mongo = new MongoConfiguration();
}
