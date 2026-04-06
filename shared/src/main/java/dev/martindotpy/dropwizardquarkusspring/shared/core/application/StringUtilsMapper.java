package dev.martindotpy.dropwizardquarkusspring.shared.core.application;

import lombok.NoArgsConstructor;

/**
 * String utils mapper.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class StringUtilsMapper {
    /**
     * Trims the given string.
     *
     * @param value the value.
     * @return the trimmed string
     */
    public static String trimString(String value) {
        return value != null ? value.trim() : null;
    }
}
