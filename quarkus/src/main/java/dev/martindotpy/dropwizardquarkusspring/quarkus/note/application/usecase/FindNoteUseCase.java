package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.usecase;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.repository.NoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FindNoteUseCase implements FindNotePort {
    private final NoteRepository noteRepository;

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll().list();
    }

    @Override
    public Optional<Note> findById(ObjectId id) {
        return noteRepository.findByIdOptional(id);
    }
}
