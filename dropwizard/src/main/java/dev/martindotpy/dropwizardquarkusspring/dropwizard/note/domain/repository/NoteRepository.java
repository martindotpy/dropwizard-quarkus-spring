package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;

public interface NoteRepository {
    List<Note> findAll();

    Optional<Note> findById(ObjectId id);

    Note create(Note note);

    Note update(Note note);

    boolean deleteById(ObjectId id);
}
