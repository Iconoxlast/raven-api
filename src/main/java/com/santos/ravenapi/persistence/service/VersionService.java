package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface VersionService {

	Optional<CharacterVersion> saveAndGetCharacterVersion(Publisher publisher, String character) throws SQLException;
	
	Optional<CharacterVersion> saveNewCharacterVersion(Publisher publisher, String character) throws SQLException;
	
	void saveNewCharacterVersions(Publisher publisher, List<String> characterVersions);

	Optional<CharacterVersion> getCharacterVersionByPageName(Long publisherId, String pageName);

	Optional<List<CharacterVersion>> getCharacterVersionsByPageNames(Long publisherId, List<String> pageNames);
	
	List<CharacterVersion> getCharacterVersionsByCharacterName(PublisherEnum publisher, String character,
			int lastUpdateLimit);

	void updateCharacterVersion(CharacterVersion updatedRecord);
}
