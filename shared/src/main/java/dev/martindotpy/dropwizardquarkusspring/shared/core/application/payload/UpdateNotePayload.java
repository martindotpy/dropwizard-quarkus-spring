package dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload;

import org.bson.types.ObjectId;

public interface UpdateNotePayload {
    ObjectId getId();

    String getContent();
}
