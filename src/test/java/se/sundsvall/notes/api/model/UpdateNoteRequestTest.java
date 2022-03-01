package se.sundsvall.notes.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class UpdateNoteRequestTest {

	@Test
	void testBean() {
		assertThat(UpdateNoteRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var body = "body";
		final var modifiedBy = "modifiedBy";
		final var subject = "subject";

		final var updateNoteRequest = UpdateNoteRequest.create()
			.withBody(body)
			.withModifiedBy(modifiedBy)
			.withSubject(subject);

		assertThat(updateNoteRequest).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(updateNoteRequest.getBody()).isEqualTo(body);
		assertThat(updateNoteRequest.getModifiedBy()).isEqualTo(modifiedBy);
		assertThat(updateNoteRequest.getSubject()).isEqualTo(subject);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(UpdateNoteRequest.create()).hasAllNullFieldsOrProperties();
	}
}
