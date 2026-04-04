package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.response;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NotNullDataResponse<T> {
    @NotNull
    private final T data;
    @NotNull
    private final String detail;

    public static <T> NotNullDataResponse<T> of(T data, String detail) {
        return new NotNullDataResponse<>(data, detail);
    }
}
