package dev.martindotpy.dropwizardquarkusspring.spring.note.application.usecase;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteNoteUseCase implements DeleteNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Optional<Boolean> delete(String id) {
        ObjectId objectId = new ObjectId(id);

        if (!noteRepository.existsById(objectId)) {
            return Optional.empty();
        }

        noteRepository.deleteById(objectId);
        return Optional.of(Boolean.TRUE);
    }
}
