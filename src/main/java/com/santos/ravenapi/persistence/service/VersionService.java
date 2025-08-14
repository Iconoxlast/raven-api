package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;

import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Publisher;

public interface VersionService {

	CharacterVersion saveCharacterVersion(Publisher publisher, String character) throws SQLException;
	
	CharacterVersion getCharacterVersionByPageName(String pageName) throws SQLException;
	
	void updateCharacterVersion(CharacterVersion updatedRecord);
}
