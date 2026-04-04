package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.adapter.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.response.NotNullDataResponse;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.response.SimpleResponse;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.view.DtoView;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notes", description = "Endpoints for managing notes.")
@Path("/api/dropwizard/note")
@RequiredArgsConstructor
public class NoteController {
    private final FindNotePort findNotePort;
    private final CreateNotePort createNotePort;
    private final UpdateNotePort updateNotePort;
    private final DeleteNotePort deleteNotePort;

    @GET
    @Operation(summary = "Get all", description = "Retrieve all notes.")
    public @JsonView(DtoView.Public.class) NotNullDataResponse<List<Note>> getAll() {
        return NotNullDataResponse.of(findNotePort.findAll(), "Notas encontradas exitosamente");
    }

    @POST
    @Operation(summary = "Create", description = "Create a new note.")
    public @JsonView(DtoView.Public.class) NotNullDataResponse<Note> create(
            @Valid @JsonView(DtoView.Create.class) Note note) {
        Note createdNote = createNotePort.create(note);

        return NotNullDataResponse.of(createdNote, "Nota creada exitosamente");
    }

    @PUT
    @Operation(summary = "Update", description = "Update an existing note.")
    public @JsonView(DtoView.Public.class) NotNullDataResponse<Note> update(
            @Valid @JsonView(DtoView.Update.class) Note note) {
        Note updatedNode = updateNotePort.update(note)
                .orElseThrow(NotFoundException::new);

        return NotNullDataResponse.of(updatedNode, "Nota actualizada exitosamente");
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete", description = "Delete an existing note.")
    public SimpleResponse delete(@PathParam("id") String id) {
        Boolean result = deleteNotePort.delete(id)
                .orElseThrow(NotFoundException::new);

        if (!result) {
            throw new InternalServerErrorException("Failed to delete note with id: " + id);
        }

        return SimpleResponse.of("Nota eliminada exitosamente");
    }
}
