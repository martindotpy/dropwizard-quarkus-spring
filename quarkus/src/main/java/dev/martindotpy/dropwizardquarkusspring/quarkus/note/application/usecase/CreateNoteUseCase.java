package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.usecase;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.mapper.NoteMapper;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.repository.NoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CreateNoteUseCase implements CreateNotePort {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public Note create(CreateNotePayload payload) {
        Note note = noteMapper.from(payload).build();

        noteRepository.persist(note);

        return note;
    }

}
