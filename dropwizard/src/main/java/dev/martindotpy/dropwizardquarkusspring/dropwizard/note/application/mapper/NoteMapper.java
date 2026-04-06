package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.StringUtilsMapper;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.CreateNotePayload;
import dev.martindotpy.dropwizardquarkusspring.shared.core.application.payload.UpdateNotePayload;

@Mapper(uses = { StringUtilsMapper.class })
public interface NoteMapper {
    @ObjectFactory
    default Note.NoteBuilder createNoteBuilder() {
        return Note.builder();
    }

    @Mapping(target = "id", ignore = true)
    Note.NoteBuilder from(CreateNotePayload payload);

    Note.NoteBuilder from(UpdateNotePayload payload);
}
