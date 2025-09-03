package com.santos.ravenapi.model.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	Optional<List<CharacterVersion>> findByCverPublisher_PublIdAndCverPageNameIn(Long publId, List<String> pageNames);

	@Query(value = """
			SELECT cv.* FROM character_disambiguations cd
			JOIN characters ch ON cd.char_id=ch.char_id
			JOIN character_versions cv ON cd.cver_id=cv.cver_id
			WHERE ch.char_publ_id=:publId
			AND ch.char_page_name=:character
			AND ch.char_latest_update >= :cutoff;
			""", nativeQuery = true)
	List<CharacterVersion> findAllByCharacterNameWithinInterval(@Param("publId") Long publId,
			@Param("character") String character, @Param("cutoff") LocalDateTime cutoff);
}
