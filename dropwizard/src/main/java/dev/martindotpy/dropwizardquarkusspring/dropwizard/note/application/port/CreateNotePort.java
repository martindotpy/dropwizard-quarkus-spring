package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;

public interface CreateNotePort {
    Note create(CreateNotePayload payload);
}
