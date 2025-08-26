package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.config.AppConfig;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.CharacterVersionRepository;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class VersionServiceImpl implements VersionService {

	@Autowired
	private CharacterVersionRepository versionRepository;

	public Optional<CharacterVersion> saveAndGetCharacterVersion(Publisher publisher, String character)
			throws SQLException {
		ArgumentValidator.validate(publisher, character);
		Optional<CharacterVersion> optCharacterVersion = null;
		try {
			optCharacterVersion = saveNewCharacterVersion(publisher, character);
		} catch (Exception e) {
			/*
			 * save() method expected to throw an exception in case a record with the same
			 * publisher ID and character version name already exists
			 */
			optCharacterVersion = getCharacterVersionByPageName(publisher.getPublId(), character);
		}
		if (optCharacterVersion.isEmpty()) {
			throw new SQLException(String.format(
					"Error in the processing of character version data.\r\nPublisher: %s\r\nCharacter Version: %s",
					publisher.getPublName(), character));
		}
		return optCharacterVersion;
	}

	public Optional<CharacterVersion> saveNewCharacterVersion(Publisher publisher, String character)
			throws SQLException {
		ArgumentValidator.validate(publisher, character);
		CharacterVersion characterVersion = new CharacterVersion(null, publisher, character,
				LocalDateTime.of(1900, 1, 1, 0, 0));
		if (!AppConfig.DEBUG_MODE) {
			versionRepository.save(characterVersion);
		}
		return Optional.of(characterVersion);
	}

	public void saveNewCharacterVersions(Publisher publisher, List<String> characterVersions) {
		ArgumentValidator.validate(publisher, characterVersions);
		if (AppConfig.DEBUG_MODE) {
			return;
		}
		List<String> newCharacterVersions = new ArrayList<>();
		List<String> existingRecords = getCharacterVersionsByPageNames(publisher.getPublId(), characterVersions).get()
				.stream().map(version -> version.getCverPageName()).toList();
		if (existingRecords.isEmpty()) {
			newCharacterVersions.addAll(characterVersions);
		} else {
			newCharacterVersions = characterVersions.stream().filter(version -> !existingRecords.contains(version))
					.toList();
		}
		if (newCharacterVersions.isEmpty()) {
			return;
		}
		versionRepository.saveAllAndFlush(newCharacterVersions.stream()
				.map(version -> new CharacterVersion(null, publisher, version, LocalDateTime.of(1900, 1, 1, 0, 0)))
				.toList());
	}

	public Optional<CharacterVersion> getCharacterVersionByPageName(Long publisherId, String pageName) {
		ArgumentValidator.validate(publisherId, pageName);
		return versionRepository.findByCverPublisher_PublIdAndCverPageName(publisherId, pageName);
	}

	public Optional<List<CharacterVersion>> getCharacterVersionsByPageNames(Long publisherId, List<String> pageNames) {
		ArgumentValidator.validate(publisherId, pageNames);
		return versionRepository.findByCverPublisher_PublIdAndCverPageNameIn(publisherId, pageNames);
	}

	public List<CharacterVersion> getCharacterVersionsByCharacterName(PublisherEnum publisher, String character,
			int lastUpdateLimit) {
		ArgumentValidator.validate(publisher, character);
		return versionRepository.findAllByCharacterNameWithinInterval(publisher.getId(), character,
				LocalDateTime.now().minusHours(lastUpdateLimit));
	}

	public void updateCharacterVersion(CharacterVersion updatedRecord) {
		ArgumentValidator.validate(updatedRecord);
		if (AppConfig.DEBUG_MODE) {
			return;
		}
		versionRepository.save(updatedRecord);
	}
}
