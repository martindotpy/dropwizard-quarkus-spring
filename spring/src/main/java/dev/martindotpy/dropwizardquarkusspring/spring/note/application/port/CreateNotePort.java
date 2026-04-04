package dev.martindotpy.dropwizardquarkusspring.spring.note.application.port;

import dev.martindotpy.dropwizardquarkusspring.spring.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;

public interface CreateNotePort {
    Note create(CreateNotePayload payload);
}
