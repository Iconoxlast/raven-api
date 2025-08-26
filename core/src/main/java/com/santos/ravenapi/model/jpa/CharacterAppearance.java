package com.santos.ravenapi.model.jpa;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity that represents an appearance of a particular version of a character,
 * and therefore joins the "issues" and "character_versions" tables. The
 * relation between these two tables is on N x N, as a character usually appears
 * in multiple issues, and an issue usually features multiple characters.
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "character_appearances")
@Entity(name = "CharacterAppearance")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "issPageId", "cverId" })
public class CharacterAppearance {
	@EmbeddedId
	private CharacterAppearanceId id;
	@ManyToOne
	@MapsId("issPageId")
	@JoinColumn(name = "iss_page_id")
	private Issue issPageId;
	@ManyToOne
	@MapsId("cverId")
	@JoinColumn(name = "cver_id")
	private CharacterVersion cverId;

	public CharacterAppearance(Issue issue, CharacterVersion characterVersion) {
		super();
		this.issPageId = issue;
		this.cverId = characterVersion;
		this.id = new CharacterAppearanceId(issue.getIssPageId(), characterVersion.getCverId());
	}
}
