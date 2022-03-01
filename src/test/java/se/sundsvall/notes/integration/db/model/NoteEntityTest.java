package se.sundsvall.notes.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class NoteEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(NoteEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var id = UUID.randomUUID().toString();
		final var partyId = UUID.randomUUID().toString();
		final var created = OffsetDateTime.now();
		final var createdBy = "createdBy";
		final var modified = OffsetDateTime.now().plusDays(1);
		final var modifiedBy = "modifiedBy";
		final var subject = "subject";
		final var body = "body";
		
		final var noteEntity = NoteEntity.create()
				.withId(id)
				.withPartyId(partyId)
				.withCreated(created)
				.withCreatedBy(createdBy)
				.withModified(modified)
				.withModifiedBy(modifiedBy)
				.withSubject(subject)
				.withBody(body);

		Assertions.assertThat(noteEntity).isNotNull().hasNoNullFieldsOrProperties();
		Assertions.assertThat(noteEntity.getId()).isEqualTo(id);
		Assertions.assertThat(noteEntity.getPartyId()).isEqualTo(partyId);
		Assertions.assertThat(noteEntity.getCreated()).isEqualTo(created);
		Assertions.assertThat(noteEntity.getCreatedBy()).isEqualTo(createdBy);
		Assertions.assertThat(noteEntity.getModified()).isEqualTo(modified);
		Assertions.assertThat(noteEntity.getModifiedBy()).isEqualTo(modifiedBy);
		Assertions.assertThat(noteEntity.getSubject()).isEqualTo(subject);
		Assertions.assertThat(noteEntity.getBody()).isEqualTo(body);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(NoteEntity.create()).hasAllNullFieldsOrProperties();
	}
}
