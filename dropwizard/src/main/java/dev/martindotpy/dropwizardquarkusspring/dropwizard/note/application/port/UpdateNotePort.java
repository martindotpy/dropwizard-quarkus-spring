package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;

public interface UpdateNotePort {
    Optional<Note> update(UpdateNotePayload payload);
}
