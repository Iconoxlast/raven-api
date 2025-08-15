package com.santos.ravenapi.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Class representing the composite key used by the CharacterDisambiguation
 * entity. It joins the "characters" and "character_versions" tables by their
 * IDs.
 * 
 * @author Joao Paulo Santos
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CharacterDisambiguationId implements Serializable {
	private static final long serialVersionUID = -6655821756717687205L;

	private Long charId;
	private Long cverId;
}
