package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.jackson.ObjectIdDeserializer;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.shared.core.domain.view.DtoView;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note implements CreateNotePayload, UpdateNotePayload {
    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonView({ DtoView.Public.class, DtoView.Update.class })
    @Schema(implementation = String.class, required = true)
    private ObjectId id;

    @NotBlank
    @JsonView({ DtoView.Public.class, DtoView.Create.class, DtoView.Update.class })
    private String content;
}
