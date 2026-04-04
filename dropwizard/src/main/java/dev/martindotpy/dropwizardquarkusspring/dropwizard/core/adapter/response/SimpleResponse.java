package dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.response;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleResponse {
    @NotNull
    private final String detail;

    public static SimpleResponse of(String detail) {
        return new SimpleResponse(detail);
    }
}
