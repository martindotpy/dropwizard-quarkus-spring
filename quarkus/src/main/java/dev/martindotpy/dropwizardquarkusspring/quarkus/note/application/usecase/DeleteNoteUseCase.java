package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.usecase;

import java.util.Optional;

import org.bson.types.ObjectId;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.repository.NoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DeleteNoteUseCase implements DeleteNotePort {
    private final NoteRepository noteRepository;

    @Override
    public Optional<Boolean> delete(String id) {
        ObjectId objectId = new ObjectId(id);

        boolean found;
        try {
            found = noteRepository.deleteById(objectId);

            if (!found) {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.of(false);
        }

        return Optional.of(found);
    }
}
