package com.santos.ravenapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.santos.ravenapi.model.jpa.Character;

/**
 * Repository for access to the "characters" table. It allows for insertion or
 * search of records regarding aliases used by one or multiple characters or
 * versions, in case the queried term in the GET request received by this API
 * leads to a disambiguation page.
 * 
 * @author Joao Paulo Santos
 */
public interface CharacterRepository extends JpaRepository<Character, Long> {

}
