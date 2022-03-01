package se.sundsvall.notes.integration.db.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;

import static java.time.OffsetDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;

@Entity
@Table(name = "note", indexes = {
	@Index(name = "note_party_id_index", columnList = "party_id")
})
public class NoteEntity {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "party_id")
	private String partyId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created")
	private OffsetDateTime created;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified")
	private OffsetDateTime modified;

	@Column(name = "subject")
	private String subject;

	@Column(name = "body")
	@Lob
	private String body;

	public static NoteEntity create() {
		return new NoteEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public NoteEntity withId(String id) {
		this.id = id;
		return this;
	}
	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public NoteEntity withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public NoteEntity withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public NoteEntity withCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public OffsetDateTime getModified() {
		return modified;
	}

	public void setModified(OffsetDateTime modified) {
		this.modified = modified;
	}

	public NoteEntity withModified(OffsetDateTime modified) {
		this.modified = modified;
		return this;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public NoteEntity withModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public NoteEntity withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public NoteEntity withBody(String body) {
		this.body = body;
		return this;
	}

	@PrePersist
	void prePersist() {
		created = now().truncatedTo(MILLIS);
	}

	@PreUpdate
	void preUpdate() {
		modified = now().truncatedTo(MILLIS);
	}

	@Override
	public int hashCode() { return Objects.hash(id, partyId, created, createdBy, modified, modifiedBy, subject, body); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoteEntity other = (NoteEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(partyId, other.partyId) && Objects.equals(created, other.created) && Objects.equals(createdBy,
			other.createdBy) && Objects.equals(modified, other.modified) && Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(subject, other.subject) &&
			Objects.equals(body, other.body);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NoteEntity [id=").append(id).append(", partyId=").append(partyId).append(", created=").append(created).append(", createdBy=").append(createdBy).append(
			", modified=").append(modified).append(", modifiedBy=").append(modifiedBy).append(", subject=").append(subject).append(", body=").append(body).append("]");
		return builder.toString();
	}
}
