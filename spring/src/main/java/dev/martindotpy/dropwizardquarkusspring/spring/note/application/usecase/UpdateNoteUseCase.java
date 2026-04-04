package dev.martindotpy.dropwizardquarkusspring.spring.note.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.martindotpy.dropwizardquarkusspring.spring.note.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateNoteUseCase implements UpdateNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Optional<Note> update(UpdateNotePayload payload) {
        Optional<Note> existing = noteRepository.findById(payload.getId());

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Note updated = existing.get();
        updated.setContent(payload.getContent().trim());

        return Optional.of(noteRepository.save(updated));
    }
}
