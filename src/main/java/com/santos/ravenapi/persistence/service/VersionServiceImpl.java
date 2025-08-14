package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.CharacterVersionRepository;

@Service
public class VersionServiceImpl implements VersionService {

	@Autowired
	private CharacterVersionRepository versionRepository;

	public CharacterVersion saveCharacterVersion(Publisher publisher, String character) throws SQLException {
		Optional<CharacterVersion> optCharacterVersion = null;
		try {
			CharacterVersion characterVersion = new CharacterVersion(null, publisher, character,
					LocalDateTime.of(1900, 1, 1, 0, 0));
			versionRepository.save(characterVersion);
			optCharacterVersion = Optional.of(characterVersion);
		} catch (Exception e) {
			/*
			 * save() method expected to throw an exception in case a record with the same
			 * publisher ID and character version name already exists
			 */
			optCharacterVersion = versionRepository.findByCverPublisher_PublIdAndCverPageName(publisher.getPublId(),
					character);
		}
		if (optCharacterVersion.isEmpty()) {
			throw new SQLException(String.format(
					"Error in the processing of character version data.\r\nPublisher: %s\r\nCharacter Version: %s",
					publisher.getPublName(), character));
		}
		return optCharacterVersion.get();
	}

	public CharacterVersion getCharacterVersionByPageName(String pageName) throws SQLException {
		Optional<CharacterVersion> characterVersion = versionRepository.findByCverPageName(pageName);
		if (characterVersion.isEmpty()) {
			throw new SQLException(String.format("Character version %s not found in the database", pageName));
		}
		return characterVersion.get();
	}
	
	public void updateCharacterVersion(CharacterVersion updatedRecord) {
		versionRepository.save(updatedRecord);
	}
}
