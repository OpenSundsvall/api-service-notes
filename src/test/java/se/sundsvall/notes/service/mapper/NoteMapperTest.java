package se.sundsvall.notes.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.Note;
import se.sundsvall.notes.api.model.UpdateNoteRequest;
import se.sundsvall.notes.integration.db.model.NoteEntity;

class NoteMapperTest {

	@Test
	void toNoteEntityFromCreateNoteRequest() {
		final var body = "body";
		final var createdBy = "createdBy";
		final var partyId = "partyId";
		final var subject = "subject";

		// Setup
		final var createNoteRequest = CreateNoteRequest.create()
			.withBody(body)
			.withCreatedBy(createdBy)
			.withPartyId(partyId)
			.withSubject(subject);

		// Call
		final var noteEntity = NoteMapper.toNoteEntity(createNoteRequest);

		// Verification
		assertThat(noteEntity.getBody()).isEqualTo(body);
		assertThat(noteEntity.getCreatedBy()).isEqualTo(createdBy);
		assertThat(noteEntity.getPartyId()).isEqualTo(partyId);
		assertThat(noteEntity.getSubject()).isEqualTo(subject);
		
		assertThat(noteEntity).extracting(
				NoteEntity::getCreated, 
				NoteEntity::getId,
				NoteEntity::getModified,
				NoteEntity::getModifiedBy).containsOnlyNulls();
	}

	@Test
	void toNoteEntityFromNull() {

		// Call
		final var webMessageEntity = NoteMapper.toNoteEntity(null);

		// Verification
		assertThat(webMessageEntity).isNull();
	}

	@Test
	void updateNoteEntityFromUpdateNoteRequest() {
		final var body = "body";
		final var created = OffsetDateTime.now();
		final var createdBy = "createdBy";
		final var id = "id";
		final var partyId = "partyId";
		final var subject = "subject";
		final var modifiedBy = "modifiedBy";
		
		// Setup
		final var noteEntity = NoteEntity.create()
				.withBody(body)
				.withCreated(created)
				.withCreatedBy(createdBy)
				.withId(id)
				.withPartyId(partyId)
				.withSubject(subject);
		
		final var updateNoteRequest = UpdateNoteRequest.create()
				.withBody(body.concat("updated"))
				.withModifiedBy(modifiedBy)
				.withSubject(subject.concat("updated"));
		
		// Call
		final var updatedNoteEntity = NoteMapper.toNoteEntity(noteEntity, updateNoteRequest);
		
		// Verification
		assertThat(updatedNoteEntity.getId()).isEqualTo(id);
		assertThat(updatedNoteEntity.getBody()).isEqualTo(body.concat("updated"));
		assertThat(updatedNoteEntity.getCreated()).isEqualTo(created);
		assertThat(updatedNoteEntity.getCreatedBy()).isEqualTo(createdBy);
		assertThat(updatedNoteEntity.getPartyId()).isEqualTo(partyId);
		assertThat(updatedNoteEntity.getSubject()).isEqualTo(subject.concat("updated"));
		assertThat(updatedNoteEntity.getModified()).isNull();
		assertThat(updatedNoteEntity.getModifiedBy()).isEqualTo(modifiedBy);
	}

	@Test
	void updateNoteEntityFromNull() {
		final var body = "body";
		final var created = OffsetDateTime.now();
		final var createdBy = "createdBy";
		final var id = "id";
		final var partyId = "partyId";
		final var subject = "subject";
		
		// Setup
		final var noteEntity = NoteEntity.create()
				.withBody(body)
				.withCreated(created)
				.withCreatedBy(createdBy)
				.withId(id)
				.withPartyId(partyId)
				.withSubject(subject);
		
		// Call
		final var updatedNoteEntity = NoteMapper.toNoteEntity(noteEntity, null);
		
		// Verification
		assertThat(updatedNoteEntity.getId()).isEqualTo(id);
		assertThat(updatedNoteEntity.getBody()).isEqualTo(body);
		assertThat(updatedNoteEntity.getCreated()).isEqualTo(created);
		assertThat(updatedNoteEntity.getCreatedBy()).isEqualTo(createdBy);
		assertThat(updatedNoteEntity.getPartyId()).isEqualTo(partyId);
		assertThat(updatedNoteEntity.getSubject()).isEqualTo(subject);
		assertThat(updatedNoteEntity.getModified()).isNull();
		assertThat(updatedNoteEntity.getModifiedBy()).isNull();
	}
	
	@Test
	void toNote() {
		final var body = "body";
		final var created = OffsetDateTime.now().minusDays(7L);
		final var createdBy = "createdBy";
		final var id = "id";
		final var modified = OffsetDateTime.now();
		final var modifiedBy = "modifiedBy";
		final var partyId = "partyId";
		final var subject = "subject";
		
		// Setup
		final var noteEntity = NoteEntity.create()
				.withBody(body)
				.withCreated(created)
				.withCreatedBy(createdBy)
				.withId(id)
				.withPartyId(partyId)
				.withSubject(subject)
				.withModified(modified)
				.withModifiedBy(modifiedBy);

		// Call
		final var note = NoteMapper.toNote(noteEntity);

		// Verification
		assertThat(note.getBody()).isEqualTo(body);
		assertThat(note.getCreated()).isEqualTo(created);
		assertThat(note.getCreatedBy()).isEqualTo(createdBy);
		assertThat(note.getId()).isEqualTo(id);
		assertThat(note.getModified()).isEqualTo(modified);
		assertThat(note.getModifiedBy()).isEqualTo(modifiedBy);
		assertThat(note.getPartyId()).isEqualTo(partyId);
		assertThat(note.getSubject()).isEqualTo(subject);
	}

	@Test
	void toNoteFromNull() {

		// Call
		final var note = NoteMapper.toNote(null);

		// Verification
		assertThat(note).isNull();
	}

	@Test
	void toNotes() {
		final var body = "body";
		final var created = OffsetDateTime.now().minusDays(7L);
		final var createdBy = "createdBy";
		final var id = "id";
		final var modified = OffsetDateTime.now();
		final var modifiedBy = "modifiedBy";
		final var partyId = "partyId";
		final var subject = "subject";
		
		// Setup
		final var noteEntity = NoteEntity.create()
				.withBody(body)
				.withCreated(created)
				.withCreatedBy(createdBy)
				.withId(id)
				.withPartyId(partyId)
				.withSubject(subject)
				.withModified(modified)
				.withModifiedBy(modifiedBy);

		// Call
		final var notes = NoteMapper.toNotes(List.of(noteEntity));

		// Verification
		assertThat(notes).hasSize(1).extracting(
				Note::getBody,
				Note::getCreated,
				Note::getCreatedBy,
				Note::getId,
				Note::getModified,
				Note::getModifiedBy,
				Note::getPartyId,
				Note::getSubject)
		.containsExactly(tuple(body, created, createdBy, id, modified, modifiedBy, partyId, subject));
	}

	@Test
	void toNotesFromNull() {

		// Call
		final var notes = NoteMapper.toNotes(null);

		// Verification
		assertThat(notes).isEmpty();
	}
}
