package dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.quarkus.core.domain.view.DtoView;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.UpdateNotePayload;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MongoEntity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note implements CreateNotePayload, UpdateNotePayload {
    @BsonId
    @JsonView({ DtoView.Public.class, DtoView.Update.class })
    @Schema(implementation = String.class, required = true)
    public ObjectId id;

    @NotBlank
    @JsonView({ DtoView.Public.class, DtoView.Create.class, DtoView.Update.class })
    public String content;
}
