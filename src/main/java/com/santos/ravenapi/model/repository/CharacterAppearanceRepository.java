package com.santos.ravenapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.ravenapi.model.jpa.CharacterAppearance;
import com.santos.ravenapi.model.jpa.CharacterAppearanceId;

/**
 * Repository for access to the "character_appearances" table. It allows for
 * insertion, removal or search of records on a particular character's
 * appearance in comic book issues.
 * 
 * @author Joao Paulo Santos
 */
public interface CharacterAppearanceRepository extends JpaRepository<CharacterAppearance, CharacterAppearanceId> {

}
