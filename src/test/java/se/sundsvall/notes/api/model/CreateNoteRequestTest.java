package se.sundsvall.notes.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class CreateNoteRequestTest {

	@Test
	void testBean() {
		assertThat(CreateNoteRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var body = "body";
		final var createdBy = "createdBy";
		final var partyId = UUID.randomUUID().toString();
		final var subject = "subject";

		final var createNoteRequest = CreateNoteRequest.create()
			.withBody(body)
			.withCreatedBy(createdBy)
			.withPartyId(partyId)
			.withSubject(subject);

		assertThat(createNoteRequest).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(createNoteRequest.getBody()).isEqualTo(body);
		assertThat(createNoteRequest.getCreatedBy()).isEqualTo(createdBy);
		assertThat(createNoteRequest.getPartyId()).isEqualTo(partyId);
		assertThat(createNoteRequest.getSubject()).isEqualTo(subject);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CreateNoteRequest.create()).hasAllNullFieldsOrProperties();
	}
}
