package dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.repository;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

public interface NoteRepository extends PanacheMongoRepository<Note> {
}
