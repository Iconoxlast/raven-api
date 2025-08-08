package com.santos.ravenapi.model.jpa;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Class representing the composite key used by the CharacterAppearance entity.
 * It joins the "issues" and "character_versions" tables by their IDs. A pageid
 * being shared across multiple Fandom wikis as an article's identifier won't be
 * an issue, considering the publisher is assigned to both the issue and the
 * version entities.
 * 
 * @author Joao Paulo Santos
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CharacterAppearanceId implements Serializable {
	private static final long serialVersionUID = -2273438213363920036L;

	private Issue issPageId;
	private CharacterVersion cverId;
}
