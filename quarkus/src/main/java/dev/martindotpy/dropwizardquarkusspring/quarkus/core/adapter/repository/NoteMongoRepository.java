package dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.repository;

import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.repository.NoteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoteMongoRepository implements NoteRepository {
}
