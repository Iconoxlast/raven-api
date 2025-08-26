package com.santos.ravenapi.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.ravenapi.model.jpa.Publisher;

/**
 * Repository for access to the "publishers" table. It allows for the search of
 * records regarding publishers the application works with. Read only; these
 * records won't be manipulated by any service.
 * 
 * @author Joao Paulo Santos
 */
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Optional<Publisher> findById(Long id);

	List<Publisher> findAll();
}
