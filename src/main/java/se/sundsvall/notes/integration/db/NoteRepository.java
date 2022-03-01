package se.sundsvall.notes.integration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.notes.integration.db.model.NoteEntity;

import java.util.List;

public interface NoteRepository extends JpaRepository<NoteEntity, String> {

	List<NoteEntity> findByPartyIdOrderByCreated(String partyId);
}
