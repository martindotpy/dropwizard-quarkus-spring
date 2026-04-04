package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.payload;

import org.bson.types.ObjectId;

public interface UpdateNotePayload {
    ObjectId getId();

    String getContent();
}
