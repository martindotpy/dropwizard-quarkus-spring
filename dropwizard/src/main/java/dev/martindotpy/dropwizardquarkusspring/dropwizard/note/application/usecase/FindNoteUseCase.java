package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindNoteUseCase implements FindNotePort {
    private final NoteRepository noteRepository;

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public Optional<Note> findById(ObjectId id) {
        return noteRepository.findById(id);
    }
}
