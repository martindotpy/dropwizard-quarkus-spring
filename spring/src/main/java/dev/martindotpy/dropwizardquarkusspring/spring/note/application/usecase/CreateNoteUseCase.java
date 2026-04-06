package dev.martindotpy.dropwizardquarkusspring.spring.note.application.usecase;

import org.springframework.stereotype.Service;

import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.mapper.NoteMapper;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateNoteUseCase implements CreateNotePort {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public Note create(CreateNotePayload payload) {
        Note note = noteMapper.from(payload).build();

        return noteRepository.save(note);
    }
}
