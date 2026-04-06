package dev.martindotpy.dropwizardquarkusspring.spring.note.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.mapper.NoteMapper;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateNoteUseCase implements UpdateNotePort {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public Optional<Note> update(UpdateNotePayload payload) {
        Note updatedNote = noteMapper.from(payload).build();

        // Search for the note to update
        Optional<Note> existing = noteRepository.findById(updatedNote.getId());

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        // Update the note
        noteRepository.save(updatedNote);

        return Optional.of(updatedNote);
    }
}
