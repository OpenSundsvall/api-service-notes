package se.sundsvall.notes.service;

import static java.lang.String.format;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.notes.service.ServiceConstants.ERROR_NOTE_NOT_FOUND;
import static se.sundsvall.notes.service.mapper.NoteMapper.toNote;
import static se.sundsvall.notes.service.mapper.NoteMapper.toNoteEntity;
import static se.sundsvall.notes.service.mapper.NoteMapper.toNotes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;

import se.sundsvall.notes.api.model.CreateNoteRequest;
import se.sundsvall.notes.api.model.Note;
import se.sundsvall.notes.api.model.UpdateNoteRequest;
import se.sundsvall.notes.integration.db.NoteRepository;
import se.sundsvall.notes.integration.db.model.NoteEntity;

@Service
public class NoteService {

	@Autowired
	private NoteRepository noteRepository;
	
	public String createNote(CreateNoteRequest createNoteRequest) {
		return noteRepository.save(toNoteEntity(createNoteRequest)).getId();
	}
	
	public Note updateNote(String id, UpdateNoteRequest updateNoteRequest) {
		NoteEntity noteEntity = noteRepository.findById(id)
				.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_NOTE_NOT_FOUND, id)));
				
		noteRepository.save(toNoteEntity(noteEntity, updateNoteRequest));
		return toNote(noteEntity);
	}
	
	public Note getNoteById(String id) {
		NoteEntity noteEntity = noteRepository.findById(id)
				.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_NOTE_NOT_FOUND, id)));
		
		return toNote(noteEntity);
	}
	
	public List<Note> getNotesByPartyId(String partyId) {
		return toNotes(noteRepository.findByPartyIdOrderByCreated(partyId));
	}
	
	public void deleteNoteById(String id) {
		if (isNotTrue(noteRepository.existsById(id))) {
			throw Problem.valueOf(NOT_FOUND, format(ERROR_NOTE_NOT_FOUND, id));
		}
		noteRepository.deleteById(id);
	}
}
