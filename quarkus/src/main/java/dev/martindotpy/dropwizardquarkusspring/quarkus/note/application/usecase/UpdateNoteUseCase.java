package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.usecase;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.mapper.NoteMapper;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.repository.NoteRepository;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UpdateNoteUseCase implements UpdateNotePort {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public Optional<Note> update(UpdateNotePayload payload) {
        Note updatedNote = noteMapper.from(payload)
                .build();

        // Search for the note to update
        Optional<Note> existingNote = noteRepository.findByIdOptional(updatedNote.id);

        if (existingNote.isEmpty()) {
            return Optional.empty();
        }

        // Update the note
        noteRepository.update(updatedNote);

        return Optional.of(updatedNote);
    }
}
