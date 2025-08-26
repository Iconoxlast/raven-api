package com.santos.ravenapi.persistence.service;

import java.util.List;
import java.util.Optional;

import com.santos.ravenapi.model.jpa.Character;
import com.santos.ravenapi.model.jpa.Publisher;

public interface CharacterService {
	
	void saveNewCharacterAliases(Publisher publisher, List<String> characterAliases);
	
	void updateCharacterLastUpdate(Character character);
	
	Optional<Character> getCharacterVersionByPageName(Long publisherId, String pageName);

}
