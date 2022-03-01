package se.sundsvall.notes.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.Note;
import se.sundsvall.notes.api.model.UpdateNoteRequest;
import se.sundsvall.notes.integration.db.model.NoteEntity;

public class NoteMapper {

	private NoteMapper() {}

	public static NoteEntity toNoteEntity(CreateNoteRequest createNoteRequest) {
		if (isNull(createNoteRequest)) {
			return null;
		}

		return NoteEntity.create()
			.withBody(createNoteRequest.getBody())
			.withCreatedBy(createNoteRequest.getCreatedBy())
			.withPartyId(createNoteRequest.getPartyId())
			.withSubject(createNoteRequest.getSubject());
	}

	public static NoteEntity toNoteEntity(NoteEntity noteEntity, UpdateNoteRequest updateNoteRequest) {
		if (isNull(updateNoteRequest)) {
			return noteEntity;
		}

		return noteEntity
			.withBody(updateNoteRequest.getBody())
			.withModifiedBy(updateNoteRequest.getModifiedBy())
			.withSubject(updateNoteRequest.getSubject());
	}

	public static Note toNote(NoteEntity noteEntity) {
		if (isNull(noteEntity)) {
			return null;
		}

		return Note.create()
			.withBody(noteEntity.getBody())
			.withCreated(noteEntity.getCreated())
			.withCreatedBy(noteEntity.getCreatedBy())
			.withId(noteEntity.getId())
			.withModified(noteEntity.getModified())
			.withModifiedBy(noteEntity.getModifiedBy())
			.withPartyId(noteEntity.getPartyId())
			.withSubject(noteEntity.getSubject());
	}

	public static List<Note> toNotes(List<NoteEntity> noteEntities) {
		return Optional.ofNullable(noteEntities).orElse(emptyList()).stream()
			.map(NoteMapper::toNote)
			.toList();
	}
}
