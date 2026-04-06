package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase;

import java.util.Optional;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.mapper.NoteMapper;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository.NoteRepository;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateNoteUseCase implements UpdateNotePort {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public Optional<Note> update(UpdateNotePayload payload) {
        Note updatedNote = noteMapper.from(payload).build();

        // Search for the note to update
        Optional<Note> existingNote = noteRepository.findById(updatedNote.getId());

        if (existingNote.isEmpty()) {
            return Optional.empty();
        }

        // Update the note
        noteRepository.update(updatedNote);

        return Optional.of(updatedNote);
    }
}
