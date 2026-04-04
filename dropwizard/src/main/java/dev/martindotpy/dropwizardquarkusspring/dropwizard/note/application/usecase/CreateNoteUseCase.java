package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository.NoteRepository;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateNoteUseCase implements CreateNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Note create(CreateNotePayload payload) {
        Note note = Note.builder()
                .content(payload.getContent().trim())
                .build();

        return noteRepository.create(note);
    }
}
