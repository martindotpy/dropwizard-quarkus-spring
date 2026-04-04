package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;

public interface CreateNotePort {
    Note create(CreateNotePayload payload);
}
