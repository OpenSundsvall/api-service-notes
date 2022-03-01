package se.sundsvall.notes.api.model;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@Schema(description = "CreateNoteRequest model")
public class CreateNoteRequest {

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1")
	@ValidUuid
	private String partyId;

	@Schema(description = "The note subject", example = "This is a subject", maximum = "256")
	@NotBlank
	@Size(min = 1, max = 256)
	private String subject;

	@Schema(description = "The note nody", example = "This is a note", maximum = "2048")
	@NotBlank
	@Size(min = 1, max = 2048)
	private String body;

	@Schema(description = "Created by", example = "John Doe", required = true)
	@NotBlank
	private String createdBy;

	public static CreateNoteRequest create() {
		return new CreateNoteRequest();
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public CreateNoteRequest withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public CreateNoteRequest withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public CreateNoteRequest withBody(String body) {
		this.body = body;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public CreateNoteRequest withCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	@Override
	public int hashCode() { return Objects.hash(body, createdBy, partyId, subject); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateNoteRequest other = (CreateNoteRequest) obj;
		return Objects.equals(body, other.body) && Objects.equals(createdBy, other.createdBy) && Objects.equals(partyId, other.partyId) && Objects.equals(subject, other.subject);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateNoteRequest [partyId=").append(partyId).append(", subject=").append(subject).append(", body=").append(body).append(", createdBy=").append(createdBy)
			.append("]");
		return builder.toString();
	}
}
