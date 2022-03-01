package se.sundsvall.notes.api.model;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "UpdateNoteRequest model")
public class UpdateNoteRequest {

	@Schema(description = "The note subject", example = "This is a subject", maximum = "256")
	@NotBlank
	@Size(min = 1, max = 256)
	private String subject;

	@Schema(description = "The note nody", example = "This is a note", maximum = "2048")
	@NotBlank
	@Size(min = 1, max = 2048)
	private String body;

	@Schema(description = "Modified by", example = "John Doe", required = true)
	@NotBlank
	private String modifiedBy;

	public static UpdateNoteRequest create() {
		return new UpdateNoteRequest();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public UpdateNoteRequest withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public UpdateNoteRequest withBody(String body) {
		this.body = body;
		return this;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public UpdateNoteRequest withModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	@Override
	public int hashCode() { return Objects.hash(body, modifiedBy, subject); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateNoteRequest other = (UpdateNoteRequest) obj;
		return Objects.equals(body, other.body) && Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(subject, other.subject);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateNoteRequest [subject=").append(subject).append(", body=").append(body).append(", modifiedBy=").append(modifiedBy).append("]");
		return builder.toString();
	}
}
