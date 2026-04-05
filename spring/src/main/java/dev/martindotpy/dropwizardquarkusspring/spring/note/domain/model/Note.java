package dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.shared.core.domain.view.DtoView;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.jackson.ObjectIdDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Document(collection = "note")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note implements CreateNotePayload, UpdateNotePayload {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonView({ DtoView.Public.class, DtoView.Update.class })
    @Schema(implementation = String.class, requiredMode = Schema.RequiredMode.REQUIRED)
    private ObjectId id;

    @NotBlank
    @JsonView({ DtoView.Public.class, DtoView.Create.class, DtoView.Update.class })
    private String content;
}
