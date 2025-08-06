package com.santos.ravenapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.ravenapi.model.jpa.CharacterDisambiguation;
import com.santos.ravenapi.model.jpa.CharacterDisambiguationId;

/**
 * Repository for access to the "character_disambiguations" table. It allows for
 * insertion, removal or search of records regarding characters' aliases and
 * their versions for disambiguation purposes.
 * 
 * @author Joao Paulo Santos
 */
public interface CharacterDisambiguationRepository
		extends JpaRepository<CharacterDisambiguation, CharacterDisambiguationId> {

}
