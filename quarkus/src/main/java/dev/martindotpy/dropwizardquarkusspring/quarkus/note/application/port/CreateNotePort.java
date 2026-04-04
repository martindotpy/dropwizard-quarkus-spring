package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;

public interface CreateNotePort {
    Note create(CreateNotePayload payload);
}
