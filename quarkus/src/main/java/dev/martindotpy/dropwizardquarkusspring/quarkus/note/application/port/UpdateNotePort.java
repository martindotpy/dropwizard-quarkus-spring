package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;

public interface UpdateNotePort {
    Optional<Note> update(UpdateNotePayload payload);
}
