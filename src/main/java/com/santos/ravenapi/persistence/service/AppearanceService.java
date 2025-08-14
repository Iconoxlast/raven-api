package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.model.jpa.CharacterAppearance;
import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Issue;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface AppearanceService {

	Optional<List<CharacterAppearance>> getCharacterAppearances(PublisherEnum publisher, String characterVersion)
			throws SQLException;

	Optional<List<IssueOutput>> getCharacterAppearancesDTO(PublisherEnum publisher, String characterVersion);

	void updateCharacterAppearances(PublisherEnum publisherEnum, String character,
			List<IssueOutput> characterAppearancesDTO) throws SQLException;

	void saveNewAppearances(List<Issue> issues, CharacterVersion character);

	void removeUnlistedAppearances(List<Issue> characterAppearances, List<Issue> recordedAppearances,
			CharacterVersion character);
}
