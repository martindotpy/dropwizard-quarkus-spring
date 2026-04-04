package dev.martindotpy.dropwizardquarkusspring.quarkus.note.adapter.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestPath;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.response.NotNullDataResponse;
import dev.martindotpy.dropwizardquarkusspring.quarkus.core.adapter.response.SimpleResponse;
import dev.martindotpy.dropwizardquarkusspring.quarkus.core.domain.view.DtoView;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.quarkus.note.domain.model.Note;
import io.quarkiverse.resteasy.problem.validation.HttpValidationProblem;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notes", description = "Endpoints for managing notes.")
@Path("/api/quarkus/note")
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
    @Operation(description = "Create a new note.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = HttpValidationProblem.class)))
    public NotNullDataResponse<Note> create(@Valid @JsonView(DtoView.Create.class) Note note) {
        Note createdNote = createNotePort.create(note);

        return NotNullDataResponse.of(createdNote, "Nota creada exitosamente");
    }

    @PUT
    @Operation(description = "Update an existing note.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = HttpValidationProblem.class)))
    public NotNullDataResponse<Note> update(@Valid @JsonView(DtoView.Update.class) Note note)
            throws NotFoundException {
        Note updatedNode = updateNotePort.update(note)
                .orElseThrow(NotFoundException::new);

        return NotNullDataResponse.of(updatedNode, "Nota actualizada exitosamente");
    }

    @DELETE
    @Path("/{id}")
    @Operation(description = "Delete an existing note.")
    public SimpleResponse delete(@RestPath String id) throws NotFoundException, InternalServerErrorException {
        var result = deleteNotePort.delete(id)
                .orElseThrow(() -> new NotFoundException());

        if (!result) {
            throw new InternalServerErrorException("Failed to delete note with id: " + id);
        }

        return SimpleResponse.of("Nota eliminada exitosamente");
    }
}
