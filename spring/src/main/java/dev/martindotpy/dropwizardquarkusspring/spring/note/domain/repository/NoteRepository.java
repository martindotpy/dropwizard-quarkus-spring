package dev.martindotpy.dropwizardquarkusspring.spring.note.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;

public interface NoteRepository extends MongoRepository<Note, ObjectId> {
}
