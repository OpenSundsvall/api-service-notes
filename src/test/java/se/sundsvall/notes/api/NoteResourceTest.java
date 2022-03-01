package se.sundsvall.notes.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import se.sundsvall.notes.Application;
import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.Note;
import se.sundsvall.notes.api.model.UpdateNoteRequest;
import se.sundsvall.notes.service.NoteService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class NoteResourceTest {

	@MockBean
	private NoteService noteService;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void createNote() {
		final var id = UUID.randomUUID().toString();
		final var expectedLocationURL = RestAssured.baseURI.concat(":").concat(String.valueOf(RestAssured.port)).concat("/notes/").concat(id);

		// Parameter values
		final var createNoteRequest = CreateNoteRequest.create()
			.withBody("Test note")
			.withCreatedBy("createdBy")
			.withPartyId(UUID.randomUUID().toString())
			.withSubject("Test subject");

		// Mock
		when(noteService.createNote(any())).thenReturn(id);

		given()
			.contentType(JSON)
			.body(createNoteRequest)
			.when()
			.post("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.CREATED.value())
			.contentType(equalTo(ALL_VALUE))
			.header(LOCATION, is(expectedLocationURL));
		
		// Verification
		verify(noteService).createNote(createNoteRequest);
	}

	@Test
	void updateNote() {
		final var id = UUID.randomUUID().toString();

		// Parameter values
		final var updateNoteRequest = UpdateNoteRequest.create()
			.withBody("Test note")
			.withModifiedBy("modifiedBy")
			.withSubject("Test subject");

		
		// Mock
		Note note = Note.create().withId(id);
		when(noteService.updateNote(id, updateNoteRequest)).thenReturn(note);
		
		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.body(updateNoteRequest)
			.when()
			.patch("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.OK.value())
			.contentType(equalTo(APPLICATION_JSON_VALUE))
			.extract().as(Note.class);

		// Verification
		assertThat(response).isNotNull().isEqualTo(note);
		verify(noteService).updateNote(id, updateNoteRequest);
	}

	@Test
	void deleteNoteById() {

		// Parameter values
		final var id = "81471222-5798-11e9-ae24-57fa13b361e1";

		given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.delete("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.NO_CONTENT.value())
			.contentType(is(emptyString()));
		
		// Verification
		verify(noteService).deleteNoteById(id);
	}

	@Test
	void getNoteById() {

		// Parameter values
		final var id = UUID.randomUUID().toString();

		// Mock
		Note note = Note.create().withId(id);
		when(noteService.getNoteById(id)).thenReturn(note);
		
		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.get("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.OK.value())
			.contentType(equalTo(APPLICATION_JSON_VALUE))
			.extract().as(Note.class);

		// Verification
		assertThat(response).isNotNull().isEqualTo(note);
		verify(noteService).getNoteById(id);
	}

	@Test
	void getNotesByPartyId() {

		// Parameter values
		final var partyId = UUID.randomUUID().toString();
		final var id = UUID.randomUUID().toString();

		// Mock
		Note note = Note.create().withId(id);
		when(noteService.getNotesByPartyId(partyId)).thenReturn(List.of(note));
		
		final var response = given()
			.contentType(JSON)
			.queryParam("partyId", partyId)
			.when()
			.get("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.OK.value())
			.contentType(equalTo(APPLICATION_JSON_VALUE))
			.extract().as(Note[].class);

		// Verification
		assertThat(response).isNotNull().hasSize(1).containsExactly(note);
		verify(noteService).getNotesByPartyId(partyId);
	}
}
