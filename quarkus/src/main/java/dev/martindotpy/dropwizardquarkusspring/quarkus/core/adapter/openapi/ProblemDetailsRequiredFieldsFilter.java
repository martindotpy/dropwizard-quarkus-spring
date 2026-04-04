package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.openapi;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;

public class ProblemDetailsRequiredFieldsFilter implements OASFilter {
    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        openAPI.getComponents().getSchemas().forEach((name, schema) -> {
            if ("HttpProblem".equals(name) || "HttpValidationProblem".equals(name)) {
                List<String> requiredFields = new ArrayList<>();

                requiredFields.add("status");
                requiredFields.add("instance");

                schema.setRequired(requiredFields);
            }

            if ("Violation".equals(name)) {
                List<String> requiredFields = new ArrayList<>();

                requiredFields.add("field");
                requiredFields.add("in");
                requiredFields.add("message");

                schema.setRequired(requiredFields);
            }
        });
    }
}
