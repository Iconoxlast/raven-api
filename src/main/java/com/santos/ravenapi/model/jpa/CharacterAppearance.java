package com.santos.ravenapi.model.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@IdClass(CharacterAppearanceId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "issPageId", "cverId" })
public class CharacterAppearance {

	@Id
	@ManyToOne
	@JoinColumn(name = "iss_page_id")
	private Issue issPageId;
	@Id
	@ManyToOne
	@JoinColumn(name = "cver_id")
	private CharacterVersion cverId;
}
