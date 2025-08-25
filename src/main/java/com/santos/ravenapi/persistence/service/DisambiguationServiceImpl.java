package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
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
	@Value("${api.character.alias.max.last.update.hours}")
	private int lastUpdateLimit;

	public Optional<DisambiguationOutput> getDisambiguationDTO(PublisherEnum publisher, String character) {
		List<CharacterVersion> characterVersions = versionService.getCharacterVersionsByCharacterName(publisher,
				character, lastUpdateLimit);
		return characterVersions.isEmpty() ? Optional.empty()
				: Optional.of(new DisambiguationOutput(
						characterVersions.stream().map(version -> version.getCverPageName()).toList()));
	}

	@Override
	public void updateDisambiguationData(PublisherEnum publisherEnum, List<String> characterAliases,
			List<String> characterVersions) throws SQLException {
		Publisher publisher = publisherService.getPublisherRecord(publisherEnum);
		versionService.saveNewCharacterVersions(publisher, characterVersions);
		characterService.saveNewCharacterAliases(publisher, characterAliases);
		saveNewDisambiguations(publisherEnum, characterAliases, characterVersions);
	}
	
	public void saveNewDisambiguations(PublisherEnum publisherEnum, List<String> characterAliases,
			List<String> characterVersions) {
		// TODO continue. need to convert these lists to their respective JPA entities via query in order to save the disambiguations
	}
}
