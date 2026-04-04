package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase;

import java.util.Optional;

import org.bson.types.ObjectId;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteNoteUseCase implements DeleteNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Optional<Boolean> delete(String id) {
        ObjectId objectId = new ObjectId(id);

        boolean deleted = noteRepository.deleteById(objectId);
        if (!deleted) {
            return Optional.empty();
        }

        return Optional.of(Boolean.TRUE);
    }
}
