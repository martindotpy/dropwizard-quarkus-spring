package dev.martindotpy.dropwizardquarkusspring.spring.note.application.port;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;

public interface FindNotePort {
    List<Note> findAll();

    Optional<Note> findById(ObjectId id);
}
