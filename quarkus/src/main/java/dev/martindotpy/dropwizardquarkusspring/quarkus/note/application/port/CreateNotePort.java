package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;

public interface CreateNotePort {
    Note create(CreateNotePayload payload);
}
