package dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.openapi;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;

public class ErrorsRequiredFieldsFilter implements OASFilter {
    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        openAPI.getComponents().getSchemas().forEach((name, schema) -> {
            if (name.startsWith("ErrorMessage")) {
                List<String> requiredFields = new ArrayList<>();

                requiredFields.add("code");
                requiredFields.add("message");

                schema.setRequired(requiredFields);
            }

            if (name.startsWith("ValidationErrorMessage")) {
                List<String> requiredFields = new ArrayList<>();

                requiredFields.add("errors");

                schema.setRequired(requiredFields);
            }
        });
    }
}
