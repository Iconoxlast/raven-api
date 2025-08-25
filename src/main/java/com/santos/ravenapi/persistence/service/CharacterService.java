package com.santos.ravenapi.persistence.service;

import java.util.List;

import com.santos.ravenapi.model.jpa.Publisher;

public interface CharacterService {
	
	void saveNewCharacterAliases(Publisher publisher, List<String> characterAliases);

}
