package com.santos.ravenapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.ravenapi.model.jpa.Issue;

/**
 * Repository for access to the "issues" table. It allows for insertion or
 * search of records regarding particular comic book issues.
 * 
 * @author Joao Paulo Santos
 */
public interface IssueRepository extends JpaRepository<Issue, Long> {

}
