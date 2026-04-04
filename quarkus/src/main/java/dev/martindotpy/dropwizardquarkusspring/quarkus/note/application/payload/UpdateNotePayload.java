package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload;

import org.bson.types.ObjectId;

public interface UpdateNotePayload {
    ObjectId getId();

    String getContent();
}
