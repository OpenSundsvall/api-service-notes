package se.sundsvall.notes.apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.LOCATION;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.notes.Application;
import se.sundsvall.notes.integration.db.NoteRepository;

/**
 * Create note apptests.
 */
@WireMockAppTestSuite(files = "classpath:/apptest/CreateNoteAppTest/", classes = Application.class)
@ActiveProfiles("junit")
class CreateNoteAppTest extends AbstractAppTest {

	@Autowired
	private NoteRepository noteRepository;

	@Test
	void test1_createNote() throws Exception {

		final var partyId = "ffd20e9d-5987-417a-b8cd-a4617ac83a88";

		assertThat(noteRepository.findByPartyIdOrderByCreated(partyId)).isEmpty();

		setupCall()
			.withServicePath("/notes")
			.withHttpMethod(HttpMethod.POST)
			.withRequest("request.json")
			.withExpectedResponseStatus(HttpStatus.CREATED)
			.withExpectedResponseHeader(LOCATION, List.of("^http://(.*)/notes/(.*)$"))
			.sendRequestAndVerifyResponse();

		assertThat(noteRepository.findByPartyIdOrderByCreated(partyId)).hasSize(1);
	}
}
