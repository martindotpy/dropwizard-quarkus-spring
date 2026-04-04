package dev.martindotpy.dropwizardquarkusspring.spring.note.adapter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;

import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.response.NotNullDataResponse;
import dev.martindotpy.dropwizardquarkusspring.spring.core.adapter.response.SimpleResponse;
import dev.martindotpy.dropwizardquarkusspring.spring.core.domain.view.DtoView;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.CreateNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.DeleteNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.FindNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.application.port.UpdateNotePort;
import dev.martindotpy.dropwizardquarkusspring.spring.note.domain.model.Note;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/spring/note")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Endpoints for managing notes.")
public class NoteController {
    private final FindNotePort findNotePort;
    private final CreateNotePort createNotePort;
    private final UpdateNotePort updateNotePort;
    private final DeleteNotePort deleteNotePort;

    @GetMapping
    @Operation(summary = "Get all", description = "Retrieve all notes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notes retrieved successfully"),
    })
    public @JsonView(DtoView.Public.class) ResponseEntity<NotNullDataResponse<List<Note>>> getAll() {
        return ResponseEntity.ok()
                .body(NotNullDataResponse.of(findNotePort.findAll(), "Notas encontradas exitosamente"));
    }

    @PostMapping
    @Operation(summary = "Create", description = "Create a new note.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Note created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    public NotNullDataResponse<Note> create(@Valid @RequestBody @JsonView(DtoView.Create.class) Note note) {
        Note createdNote = createNotePort.create(note);
        return NotNullDataResponse.of(createdNote, "Nota creada exitosamente");
    }

    @PutMapping
    @Operation(summary = "Update", description = "Update an existing note.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Note updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Note not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public NotNullDataResponse<Note> update(@Valid @RequestBody @JsonView(DtoView.Update.class) Note note) {
        Note updatedNote = updateNotePort.update(note)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return NotNullDataResponse.of(updatedNote, "Nota actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete", description = "Delete an existing note.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public SimpleResponse delete(@PathVariable String id) {
        Boolean result = deleteNotePort.delete(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!result) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete note with id: " + id);
        }

        return SimpleResponse.of("Nota eliminada exitosamente");
    }
}
