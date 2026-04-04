package dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.repository;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.repository.NoteRepository;

public class NoteMongoRepository implements NoteRepository {
    private final MongoCollection<Note> noteCollection;

    public NoteMongoRepository(MongoCollection<Note> noteCollection) {
        this.noteCollection = noteCollection;

        Document idIndex = new Document("_id", 1);
        this.noteCollection.createIndex(idIndex);
    }

    @Override
    public List<Note> findAll() {
        return noteCollection.find().into(new ArrayList<>());
    }

    @Override
    public Optional<Note> findById(ObjectId id) {
        return Optional.ofNullable(noteCollection.find(eq("_id", id)).first());
    }

    @Override
    public Note create(Note note) {
        if (note.getId() == null) {
            note.setId(new ObjectId());
        }

        noteCollection.insertOne(note);

        return note;
    }

    @Override
    public Note update(Note note) {
        return noteCollection.findOneAndReplace(
                eq("_id", note.getId()),
                note,
                new FindOneAndReplaceOptions()
                        .returnDocument(ReturnDocument.AFTER)
                        .upsert(false));
    }

    @Override
    public boolean deleteById(ObjectId id) {
        return noteCollection.deleteOne(eq("_id", id)).getDeletedCount() > 0;
    }
}
