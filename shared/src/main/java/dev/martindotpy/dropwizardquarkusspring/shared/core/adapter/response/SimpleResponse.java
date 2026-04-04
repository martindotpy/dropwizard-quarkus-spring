package dev.martindotpy.dropwizardquarkusspring.shared.core.adapter.response;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.shared.core.domain.view.DtoView;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleResponse {
    @JsonView(DtoView.Public.class)
    private final @NotNull String detail;

    public static SimpleResponse of(String detail) {
        return new SimpleResponse(detail);
    }
}
