package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateNoteUseCase implements UpdateNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Optional<Note> update(UpdateNotePayload payload) {
        Optional<Note> existingNote = noteRepository.findById(payload.getId());

        if (existingNote.isEmpty()) {
            return Optional.empty();
        }

        Note updated = existingNote.get();
        updated.setContent(payload.getContent().trim());

        return Optional.ofNullable(noteRepository.update(updated));
    }
}
