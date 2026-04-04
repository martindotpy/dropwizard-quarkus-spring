package dev.martindotpy.dropwizardquarkusspring.spring.note.application.usecase;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@Service
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
