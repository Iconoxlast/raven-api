package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.config.AppConfig;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.model.jpa.Character;
import com.santos.ravenapi.model.jpa.CharacterDisambiguation;
import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.CharacterDisambiguationRepository;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class DisambiguationServiceImpl implements DisambiguationService {

	@Autowired
	private VersionService versionService;
	@Autowired
	private PublisherService publisherService;
	@Autowired
	private CharacterService characterService;
	@Autowired
	private CharacterDisambiguationRepository disambiguationRepository;
	private static final Logger logger = LoggerFactory.getLogger(DisambiguationServiceImpl.class);
	@Value("${api.character.alias.max.last.update.hours}")
	private int lastUpdateLimit;

	public Optional<DisambiguationOutput> getDisambiguationDTO(PublisherEnum publisher, String character) {
		ArgumentValidator.validate(publisher, character);
		List<CharacterVersion> characterVersions = versionService.getCharacterVersionsByCharacterName(publisher,
				character, lastUpdateLimit);
		return characterVersions.isEmpty() ? Optional.empty()
				: Optional.of(new DisambiguationOutput(
						characterVersions.stream().map(CharacterVersion::getCverPageName).toList()));
	}

	public void updateDisambiguationData(PublisherEnum publisherEnum, List<String> characterAliases,
			List<String> characterVersions) throws SQLException {
		if (AppConfig.DEBUG_MODE) {
			return;
		}
		ArgumentValidator.validate(publisherEnum, characterAliases, characterVersions);
		Publisher publisher = publisherService.getPublisherRecord(publisherEnum);
		versionService.saveNewCharacterVersions(publisher, characterVersions);
		characterService.saveNewCharacterAliases(publisher, characterAliases);
		processNewDisambiguations(publisherEnum, characterAliases, characterVersions);
	}

	private void processNewDisambiguations(PublisherEnum publisherEnum, List<String> characterAliases,
			List<String> characterVersions) {
		try {
			List<CharacterVersion> versions = getCharacterVersions(publisherEnum, characterVersions);
			characterAliases.forEach(alias -> {
				try {
					Character characterAlias = getCharacterAlias(publisherEnum, alias);
					saveDisambiguations(characterAlias, versions);
					characterService.updateCharacterLastUpdate(characterAlias);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private List<CharacterVersion> getCharacterVersions(PublisherEnum publisherEnum, List<String> versions)
			throws InterruptedException {
		Optional<List<CharacterVersion>> characterVersions = versionService
				.getCharacterVersionsByPageNames(publisherEnum.getId(), versions);
		if (versions.isEmpty()) {
			throw new InterruptedException(String.format(
					"Failed to obtain character versions data from the database. Disambiguation data not saved. %s %s",
					publisherEnum, String.join(", ", versions)));
		}
		return characterVersions.get();
	}

	private Character getCharacterAlias(PublisherEnum publisherEnum, String alias) throws InterruptedException {
		Optional<Character> characterAlias = characterService.getCharacterVersionByPageName(publisherEnum.getId(),
				alias);
		if (characterAlias.isEmpty()) {
			throw new InterruptedException(String.format(
					"Failed to obtain character alias data from the database. Disambiguation data not saved. %s %s",
					publisherEnum, alias));
		}
		return characterAlias.get();
	}

	private void saveDisambiguations(Character characterAlias, List<CharacterVersion> characterVersions) {
		Optional<List<CharacterDisambiguation>> optExistingRecords = disambiguationRepository
				.findByCharId_CharId(characterAlias.getCharId());
		List<CharacterDisambiguation> existingRecords = optExistingRecords.isPresent() ? optExistingRecords.get()
				: new ArrayList<>();
		List<CharacterDisambiguation> newDisambiguations = characterVersions.stream().filter(
				version -> !existingRecords.stream().map(CharacterDisambiguation::getCverId).toList().contains(version))
				.map(version -> new CharacterDisambiguation(characterAlias, version)).toList();
		if (newDisambiguations.isEmpty()) {
			return;
		}
		disambiguationRepository.saveAll(newDisambiguations);
	}
}
