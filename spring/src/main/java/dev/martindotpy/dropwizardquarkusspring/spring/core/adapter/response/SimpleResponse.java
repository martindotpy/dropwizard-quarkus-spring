package dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.response;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.spring.core.domain.view.DtoView;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleResponse {
    @NotNull
    @JsonView(DtoView.Public.class)
    private final String detail;

    public static SimpleResponse of(String detail) {
        return new SimpleResponse(detail);
    }
}
