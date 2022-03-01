package se.sundsvall.notes.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Problem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.Note;
import se.sundsvall.notes.api.model.UpdateNoteRequest;
import se.sundsvall.notes.service.NoteService;

@RestController
@Validated
@RequestMapping("/notes")
@Tag(name = "Notes", description = "Notes")
public class NotesResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotesResource.class);

	@Autowired
	private NoteService noteService;
	
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = { APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Create new note")
	@ApiResponse(responseCode = "201", headers = @Header(name = LOCATION, schema = @Schema(type = "string")), description = "Successful operation", content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Void> createNote(UriComponentsBuilder uriComponentsBuilder, @Valid @NotNull @RequestBody CreateNoteRequest body) {
		LOGGER.debug("Received createNote()-request: body='{}'", body);

		return ResponseEntity
			.created(uriComponentsBuilder.path("/notes/{id}").buildAndExpand(noteService.createNote(body)).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Update note")
	@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Note.class)))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Note> updateNote(
		@Parameter(name = "id", description = "Note ID", example = "b82bd8ac-1507-4d9a-958d-369261eecc15") @ValidUuid @PathVariable(name = "id", required = true) String id,
		@Valid @NotNull @RequestBody UpdateNoteRequest body) {
		LOGGER.debug("Received updateNote()-request: body='{}'", body);

		return ResponseEntity.ok(noteService.updateNote(id, body));
	}

	@GetMapping(path = "/{id}", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Get note by ID")
	@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Note.class)))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Note> getNoteById(
		@Parameter(name = "id", description = "Note ID", example = "b82bd8ac-1507-4d9a-958d-369261eecc15") @ValidUuid @PathVariable(name = "id", required = true) String id) {
		LOGGER.debug("Received getNoteById()-request: id='{}'", id);

		return ResponseEntity.ok(noteService.getNoteById(id));
	}

	@GetMapping(produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Get all notes, filtered by partyId")
	@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Note.class))))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<List<Note>> getNotesByPartyId(
		@Parameter(name = "partyId", description = "PartyID", example = "81471222-5798-11e9-ae24-57fa13b361e1") @RequestParam(value = "partyId", required = true) @ValidUuid String partyId) {
		LOGGER.debug("Received getNotesByPartyId()-request: partyId='{}'", partyId);

		return ResponseEntity.ok(noteService.getNotesByPartyId(partyId));
	}

	@DeleteMapping(path = "/{id}", produces = { APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Delete note by ID")
	@ApiResponse(responseCode = "204", description = "Successful operation", content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Void> deleteNoteById(
		@Parameter(name = "id", description = "Note ID", example = "b82bd8ac-1507-4d9a-958d-369261eecc15") @ValidUuid @PathVariable String id) {
		LOGGER.debug("Received deleteNoteById-request: id='{}'", id);
		
		noteService.deleteNoteById(id);
		return ResponseEntity.noContent().build();
	}
}
