package dev.martindotpy.dropwizardquarkusspring.spring.note.application.usecase;

import org.springframework.stereotype.Service;

import dev.martindotpy.dropwizardquarkusspring.spring.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateNoteUseCase implements CreateNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Note create(CreateNotePayload payload) {
        Note note = Note.builder()
                .content(payload.getContent().trim())
                .build();

        return noteRepository.save(note);
    }
}
