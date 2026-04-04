package dev.martindotpy.dropwizardquarkusspring.spring.note.application.port;

import java.util.Optional;

public interface DeleteNotePort {
    Optional<Boolean> delete(String id);
}
