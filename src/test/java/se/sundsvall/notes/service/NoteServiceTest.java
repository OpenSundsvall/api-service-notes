package se.sundsvall.notes.service;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.notes.service.ServiceConstants.ERROR_NOTE_NOT_FOUND;
import static se.sundsvall.notes.service.mapper.NoteMapper.toNoteEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.Note;
import se.sundsvall.notes.api.model.UpdateNoteRequest;
import se.sundsvall.notes.integration.db.NoteRepository;
import se.sundsvall.notes.integration.db.model.NoteEntity;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

	@Mock
	private NoteRepository noteRepository;

	@InjectMocks
	private NoteService noteService;

	@Captor
	ArgumentCaptor<NoteEntity> noteEntityCaptor;

	@Test
	void createNote() {
		final var body = "body";
		final var createdBy = "createdBy";
		final var partyId = "partyId";
		final var subject = "subject";
		final var id = UUID.randomUUID().toString();
		final var created = OffsetDateTime.now();

		// Setup
		final var createNoteRequest = CreateNoteRequest.create()
			.withBody(body)
			.withCreatedBy(createdBy)
			.withPartyId(partyId)
			.withSubject(subject);

		// Mock
		final var noteEntity = toNoteEntity(createNoteRequest)
			.withId(id)
			.withCreated(created);

		when(noteRepository.save(any())).thenReturn(noteEntity);

		// Call
		final var result = noteService.createNote(createNoteRequest);

		// Verification
		verify(noteRepository).save(noteEntityCaptor.capture());

		assertThat(noteEntityCaptor.getValue())
			.extracting(
				NoteEntity::getBody,
				NoteEntity::getCreated,
				NoteEntity::getCreatedBy,
				NoteEntity::getId,
				NoteEntity::getModified,
				NoteEntity::getModifiedBy,
				NoteEntity::getPartyId,
				NoteEntity::getSubject)
			.containsExactly(body, null, createdBy, null, null, null, partyId, subject);

		assertThat(result).isEqualTo(id);
	}

	@Test
	void updateNote() {

		final var id = UUID.randomUUID().toString();
		final var body = "Body";
		final var modifiedBy = "modifiedBy";
		final var subject = "Subject";

		// Setup
		final var updateNoteRequest = UpdateNoteRequest.create()
			.withBody(body)
			.withModifiedBy(modifiedBy)
			.withSubject(subject);

		// Mock
		when(noteRepository.findById(id)).thenReturn(Optional.of(NoteEntity.create().withId(id)));

		// Call
		final var result = noteService.updateNote(id, updateNoteRequest);

		// Verification
		verify(noteRepository).save(noteEntityCaptor.capture());

		assertThat(noteEntityCaptor.getValue())
			.extracting(
				NoteEntity::getBody,
				NoteEntity::getCreated,
				NoteEntity::getCreatedBy,
				NoteEntity::getId,
				NoteEntity::getModified,
				NoteEntity::getModifiedBy,
				NoteEntity::getPartyId,
				NoteEntity::getSubject)
			.containsExactly(body, null, null, id, null, modifiedBy, null, subject);
		assertThat(result).isNotNull().extracting(Note::getId).isEqualTo(id);
	}

	@Test
	void updateNoteIdNotFound() {

		// Setup
		final var id = UUID.randomUUID().toString();
		final var request = UpdateNoteRequest.create();

		// Mock
		when(noteRepository.findById(id)).thenReturn(Optional.empty());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> noteService.updateNote(id, request));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_NOTE_NOT_FOUND, id));
		verify(noteRepository).findById(id);
	}

	@Test
	void deleteNoteById() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(noteRepository.existsById(id)).thenReturn(true);

		// Call
		noteService.deleteNoteById(id);

		// Verification
		verify(noteRepository).existsById(id);
		verify(noteRepository).deleteById(id);
	}

	@Test
	void deleteNoteByIdNotFound() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> noteService.deleteNoteById(id));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_NOTE_NOT_FOUND, id));
		verify(noteRepository).existsById(id);
	}

	@Test
	void getNoteById() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(noteRepository.findById(id)).thenReturn(Optional.of(NoteEntity.create().withId(id)));

		// Call
		final var result = noteService.getNoteById(id);

		// Verification
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(id);
		verify(noteRepository).findById(id);
	}

	@Test
	void getNoteByIdNotFound() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(noteRepository.findById(id)).thenReturn(Optional.empty());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> noteService.getNoteById(id));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_NOTE_NOT_FOUND, id));
		verify(noteRepository).findById(id);
	}

	@Test
	void getNotesByPartyId() {

		// Setup
		final var id = UUID.randomUUID().toString();
		final var partyId = UUID.randomUUID().toString();

		// Mock
		when(noteRepository.findByPartyIdOrderByCreated(partyId)).thenReturn(List.of(NoteEntity.create().withId(id).withPartyId(partyId)));

		// Call
		final var result = noteService.getNotesByPartyId(partyId);

		// Verification
		assertThat(result).isNotNull().hasSize(1).extracting(
			Note::getId,
			Note::getPartyId)
			.containsExactly(tuple(id, partyId));

		verify(noteRepository).findByPartyIdOrderByCreated(partyId);
	}
}
