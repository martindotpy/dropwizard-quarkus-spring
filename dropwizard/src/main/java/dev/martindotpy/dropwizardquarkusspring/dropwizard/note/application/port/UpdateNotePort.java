package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;

public interface UpdateNotePort {
    Optional<Note> update(UpdateNotePayload payload);
}
