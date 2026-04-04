package dev.martindotpy.dropwizardquarkusspring.spring.note.application.port;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.spring.note.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;

public interface UpdateNotePort {
    Optional<Note> update(UpdateNotePayload payload);
}
