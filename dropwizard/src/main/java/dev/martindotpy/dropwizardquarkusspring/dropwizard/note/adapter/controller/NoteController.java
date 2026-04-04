package dev.martindotpy.dropwizardquarkusspring.dropwizard.note.adapter.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.response.NotNullDataResponse;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.response.SimpleResponse;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import dev.martindotpy.dropwizardquarkusspring.shared.core.domain.view.DtoView;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all", description = "Retrieve all notes.")
    @APIResponse(responseCode = "200", description = "Notes retrieved successfully")
    public @JsonView(DtoView.Public.class) NotNullDataResponse<List<Note>> getAll() {
        return NotNullDataResponse.of(findNotePort.findAll(), "Notas encontradas exitosamente");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create", description = "Create a new note.")
    @APIResponse(responseCode = "200", description = "Note created successfully")
    @APIResponse(responseCode = "422", description = "Validation error", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ValidationErrorMessage.class)))
    public @JsonView(DtoView.Public.class) NotNullDataResponse<Note> create(
            @Valid @JsonView(DtoView.Create.class) Note note) {
        Note createdNote = createNotePort.create(note);

        return NotNullDataResponse.of(createdNote, "Nota creada exitosamente");
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update", description = "Update an existing note.")
    @APIResponse(responseCode = "200", description = "Note updated successfully")
    @APIResponse(responseCode = "404", description = "Note not found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorMessage.class)))
    @APIResponse(responseCode = "422", description = "Validation error", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ValidationErrorMessage.class)))
    public @JsonView(DtoView.Public.class) NotNullDataResponse<Note> update(
            @Valid @JsonView(DtoView.Update.class) Note note) {
        Note updatedNode = updateNotePort.update(note)
                .orElseThrow(NotFoundException::new);

        return NotNullDataResponse.of(updatedNode, "Nota actualizada exitosamente");
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete", description = "Delete an existing note.")
    @APIResponse(responseCode = "200", description = "Note deleted successfully")
    @APIResponse(responseCode = "404", description = "Note not found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorMessage.class)))
    @APIResponse(responseCode = "422", description = "Validation error", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ValidationErrorMessage.class)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorMessage.class)))
    public SimpleResponse delete(
            @Valid @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Invalid note ID format. Must be a 24-character hexadecimal string.") @PathParam("id") String id)
            throws NotFoundException, InternalServerErrorException {
        Boolean result = deleteNotePort.delete(id)
                .orElseThrow(NotFoundException::new);

        if (!result) {
            throw new InternalServerErrorException("Failed to delete note with id: " + id);
        }

        return SimpleResponse.of("Nota eliminada exitosamente");
    }
}
