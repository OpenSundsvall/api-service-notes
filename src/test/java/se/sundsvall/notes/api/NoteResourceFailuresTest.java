package se.sundsvall.notes.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

import io.restassured.RestAssured;
import se.sundsvall.notes.Application;
import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.UpdateNoteRequest;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class NoteResourceFailuresTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void createNoteMissingPartyId() {

		// Parameter values
		final var createNoteRequest = CreateNoteRequest.create()
			.withBody("Test note")
			.withCreatedBy("createdBy")
			.withPartyId(null) // Missing partyId
			.withSubject("subject");

		final var response = given()
			.contentType(JSON)
			.body(createNoteRequest)
			.when()
			.post("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");
	}

	@Test
	void createNoteInvalidPartyId() {

		// Parameter values
		final var createNoteRequest = CreateNoteRequest.create()
			.withBody("Test note")
			.withCreatedBy("createdBy")
			.withPartyId("invalid")
			.withSubject("subject");

		final var response = given()
			.contentType(JSON)
			.body(createNoteRequest)
			.when()
			.post("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");
	}

	@Test
	void createNoteToLongParameterValues() {

		// Parameter values
		final var createNoteRequest = CreateNoteRequest.create()
			.withBody(repeat("*", 2049))
			.withCreatedBy("createdBy")
			.withPartyId(UUID.randomUUID().toString())
			.withSubject(repeat("*", 257));

		final var response = given()
			.contentType(JSON)
			.body(createNoteRequest)
			.when()
			.post("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("body", "subject");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("size must be between 1 and 2048", "size must be between 1 and 256");
	}

	@Test
	void createNoteEmptyJsonBody() {

		// Parameter values
		final var createNoteRequest = "{}";

		final var response = given()
			.contentType(JSON)
			.body(createNoteRequest)
			.when()
			.post("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("body", "createdBy", "partyId", "subject");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("must not be blank", "must not be blank", "not a valid UUID", "must not be blank");
	}

	@Test
	void createNoteNullBody() {

		final var response = given()
			.contentType(JSON)
			.when()
			.post("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(Problem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getDetail()).contains("Required request body is missing");
	}

	@Test
	void updateNoteInvalidId() {

		// Parameter values
		final var id = "invalid";
		final var updateNoteRequest = UpdateNoteRequest.create()
			.withBody("body")
			.withSubject("subject")
			.withModifiedBy("modifiedBy");

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.body(updateNoteRequest)
			.when()
			.patch("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("updateNote.id");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");
	}

	@Test
	void updateNoteMissingProperties() {

		// Parameter values
		final var id = UUID.randomUUID().toString();
		final var updateNoteRequest = UpdateNoteRequest.create();

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.body(updateNoteRequest)
			.when()
			.patch("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("body", "modifiedBy", "subject");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("must not be blank", "must not be blank", "must not be blank");
	}

	@Test
	void updateNoteTooLongParameterValues() {

		// Parameter values
		final var id = UUID.randomUUID().toString();
		final var updateNoteRequest = UpdateNoteRequest.create()
			.withBody(repeat("*", 2049))
			.withModifiedBy("modifiedBy")
			.withSubject(repeat("*", 257));

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.body(updateNoteRequest)
			.when()
			.patch("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("body", "subject");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("size must be between 1 and 2048", "size must be between 1 and 256");
	}

	@Test
	void updateNoteNullBody() {

		// Parameter values
		final var id = UUID.randomUUID().toString();

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.patch("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(Problem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getDetail()).contains("Required request body is missing");
	}

	@Test
	void getNoteByIdInvalidId() {

		// Parameter values
		final var id = "invalid";

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.get("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("getNoteById.id");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");
	}

	@Test
	void getNotesByPartyIdInvalidPartyId() {

		// Parameter values
		final var partyId = "invalid";

		final var response = given()
			.contentType(JSON)
			.queryParam("partyId", partyId)
			.when()
			.get("/notes")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("getNotesByPartyId.partyId");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");
	}

	@Test
	void deleteNoteByIdInvalidId() {

		// Parameter values
		final var id = "invalid";

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.delete("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getViolations()).extracting(Violation::getField).containsExactly("deleteNoteById.id");
		assertThat(response.getViolations()).extracting(Violation::getMessage).containsExactly("not a valid UUID");
	}

	@Test
	void deleteNoteByIdEmptyId() {

		// Parameter values
		final var id = "";

		final var response = given()
			.contentType(JSON)
			.pathParam("id", id)
			.when()
			.delete("/notes/{id}")
			.then().assertThat()
			.statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
			.contentType(equalTo(APPLICATION_PROBLEM_JSON_VALUE))
			.extract().as(ConstraintViolationProblem.class);

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus().getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
		assertThat(response.getViolations()).isEmpty();
	}
}
