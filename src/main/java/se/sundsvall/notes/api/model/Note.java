package se.sundsvall.notes.api.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "CreateNoteRequest model")
public class Note {

	@Schema(description = "Note ID", example = "b82bd8ac-1507-4d9a-958d-369261eecc15")
	private String id;

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1")
	private String partyId;

	@Schema(description = "The note subject", example = "This is a subject")
	private String subject;

	@Schema(description = "The note nody", example = "This is a note")
	private String body;

	@Schema(description = "Created by", example = "John Doe")
	private String createdBy;

	@Schema(description = "Modified by", example = "John Doe")
	private String modifiedBy;

	@Schema(description = "Created timestamp")
	private OffsetDateTime created;

	@Schema(description = "Modified timestamp")
	private OffsetDateTime modified;

	public static Note create() {
		return new Note();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Note withId(String id) {
		this.id = id;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Note withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Note withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Note withBody(String body) {
		this.body = body;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Note withCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Note withModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public Note withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public OffsetDateTime getModified() {
		return modified;
	}

	public void setModified(OffsetDateTime modified) {
		this.modified = modified;
	}

	public Note withModified(OffsetDateTime modified) {
		this.modified = modified;
		return this;
	}

	@Override
	public int hashCode() { return Objects.hash(body, created, createdBy, id, modified, modifiedBy, partyId, subject); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		return Objects.equals(body, other.body) && Objects.equals(created, other.created) && Objects.equals(createdBy, other.createdBy) && Objects.equals(id, other.id) && Objects
			.equals(modified, other.modified) && Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(partyId, other.partyId) && Objects.equals(subject, other.subject);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Note [id=").append(id).append(", partyId=").append(partyId).append(", subject=").append(subject).append(", body=").append(body).append(", createdBy=")
			.append(createdBy).append(", modifiedBy=").append(modifiedBy).append(", created=").append(created).append(", modified=").append(modified).append("]");
		return builder.toString();
	}
}
