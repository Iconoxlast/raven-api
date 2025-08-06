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
 * Entity representing the relationship between a character's alias and its
 * version, joining the "characters" and "character_versions" tables. The
 * relation is on N x N because an alias will be shared by multiple characters
 * (ex. "Hal Jordan," the character, has several versions across multiple
 * realities and media), and a character version may have had multiple aliases
 * (ex. "Hal Jordan (New Earth)" has "Hal Jordan", "Parallax", "The Spectre",
 * and others listed as aliases). This entity is employed in case the queried
 * term in the client's GET request leads to a disambiguation page.
 * 
 * This table's records must be updated periodically, to ensure that the
 * versions being listed in the output DTO are consistent with the Fandom wiki.
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "character_disambiguations")
@Entity(name = "CharacterDisambiguation")
@IdClass(CharacterDisambiguationId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "character", "characterVersion" })
public class CharacterDisambiguation {

	@Id
	@ManyToOne
	@JoinColumn(name = "char_id")
	private Character character;
	@Id
	@ManyToOne
	@JoinColumn(name = "cver_id")
	private CharacterVersion characterVersion;
}
