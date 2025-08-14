package com.santos.ravenapi.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.santos.ravenapi.model.jpa.Issue;

/**
 * Repository for access to the "issues" table. It allows for insertion or
 * search of records regarding particular comic book issues.
 * 
 * @author Joao Paulo Santos
 */
public interface IssueRepository extends JpaRepository<Issue, Long> {

	@Query(value = """
			SELECT i FROM character_appearances ca
			JOIN issues i ON ca.iss_page_id=i.iss_page_id
			WHERE i.iss_publ_id=:publId
			AND ca.cver_id=:cverId
			ORDER BY i.iss_page_id
			""", nativeQuery = true)
	List<Issue> findAllByCharacterAppearances(Long publId, Long cverId);

	@Query(value = """
			SELECT i.* FROM issues i
			JOIN character_appearances ca ON i.iss_page_id=ca.iss_page_id
			JOIN character_versions cv ON cv.cver_id=ca.cver_id
			JOIN publishers pb ON pb.publ_id=i.iss_publ_id
			WHERE pb.publ_id=:publId
			AND cv.cver_page_name=:characterVersion
			AND cver_latest_update >= :cutoff
			ORDER BY i.iss_publication_date, i.iss_page_name
			""", nativeQuery = true)
	List<Issue> findAllByCharacterVersionWithinInterval(Long publId, String characterVersion, LocalDateTime cutoff);
}
