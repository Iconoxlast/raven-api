package com.santos.ravenapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.ravenapi.model.jpa.CharacterVersion;

/**
 * Repository for access to the "character_versions" table. It allows for
 * insertion or search of records regarding particular versions of comic book
 * characters across different realities or media.
 * 
 * @author Joao Paulo Santos
 */
public interface CharacterVersionRepository extends JpaRepository<CharacterVersion, Long> {

	Optional<CharacterVersion> findByCverPublisher_PublIdAndCverPageName(Long publId, String pageName);
	
	Optional<CharacterVersion> findByCverPageName(String pageName);
}
