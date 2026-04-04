package dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import dev.martindotpy.dropwizardquarkusspring.quarkus.core.application.StringUtilsMapper;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.payload.UpdateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;

@Mapper(componentModel = "cdi", uses = { StringUtilsMapper.class })
public interface NoteMapper {
    @ObjectFactory
    default Note.NoteBuilder createNoteBuilder() {
        return Note.builder();
    }

    @Mapping(target = "id", ignore = true)
    Note.NoteBuilder from(CreateNotePayload payload);

    Note.NoteBuilder from(UpdateNotePayload payload);
}
