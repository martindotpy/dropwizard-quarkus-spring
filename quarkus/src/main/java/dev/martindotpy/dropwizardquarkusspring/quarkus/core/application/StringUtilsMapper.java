package dev.martindotpy.dropwizardquarkusspring.quarkus.core.application;

/**
 * String utils mapper.
 */
public class StringUtilsMapper {
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
