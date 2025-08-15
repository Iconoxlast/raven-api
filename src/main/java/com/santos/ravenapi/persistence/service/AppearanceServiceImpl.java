package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.config.AppConfig;
import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.model.jpa.CharacterAppearance;
import com.santos.ravenapi.model.jpa.CharacterVersion;
import com.santos.ravenapi.model.jpa.Issue;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.CharacterAppearanceRepository;
import com.santos.ravenapi.model.repository.PublisherRepository;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class AppearanceServiceImpl implements AppearanceService {

	@Autowired
	private CharacterAppearanceRepository appearanceRepository;
	@Autowired
	private PublisherRepository publisherRepository;
	@Autowired
	private IssueService issueService;
	@Autowired
	private VersionService versionService;
	@Value("${api.character.version.max.last.update.hours}")
	private int lastUpdateLimit;

	// may not be used
	public Optional<List<CharacterAppearance>> getAppearances(PublisherEnum publisher, String characterVersion)
			throws SQLException {
		return Optional.of(convertIssuesListToCharacterAppearancesList(
				issueService.getUpToDateAppearanceIssues(publisher, characterVersion, lastUpdateLimit),
				versionService.getCharacterVersionByPageName(characterVersion)));
	}

	public Optional<List<IssueOutput>> getAppearancesDTO(PublisherEnum publisher, String characterVersion) {
		return Optional.of(issueService.convertEntityListToDtoList(
				issueService.getUpToDateAppearanceIssues(publisher, characterVersion, lastUpdateLimit)));
	}

	public void updateCharacterAppearances(PublisherEnum publisherEnum, String character,
			List<IssueOutput> characterAppearancesDTO) throws SQLException {
		if (characterAppearancesDTO.isEmpty() || AppConfig.DEBUG_MODE) {
			return;
		}
		Publisher publisher = getPublisherRecord(publisherEnum);
		CharacterVersion characterVersion = versionService.saveCharacterVersion(publisher, character);

		List<Issue> characterAppearances = issueService.convertDtoListToEntityList(publisher, characterAppearancesDTO);
		List<Issue> recordedAppearances = issueService.getRecordedCharacterAppearances(publisherEnum,
				characterVersion.getCverId());

		saveNewAppearances(issueService.saveNewIssues(characterAppearances, recordedAppearances), characterVersion);
		removeUnlistedAppearances(characterAppearances, recordedAppearances, characterVersion);
		characterVersion.setCverLatestUpdate(LocalDateTime.now());
		versionService.updateCharacterVersion(characterVersion);
	}

	private Publisher getPublisherRecord(PublisherEnum publisherEnum) throws SQLException {
		Optional<Publisher> publisher = publisherRepository.findById(publisherEnum.getId());
		if (publisher.isEmpty()) {
			throw new SQLException("Publisher %s's record not found in the database.", publisherEnum.toString());
		}
		return publisher.get();
	}

	public void saveNewAppearances(List<Issue> issues, CharacterVersion character) {
		if (AppConfig.DEBUG_MODE) {
			return;
		}
		appearanceRepository.saveAll(convertIssuesListToCharacterAppearancesList(issues, character));
	}

	private List<CharacterAppearance> convertIssuesListToCharacterAppearancesList(List<Issue> issues,
			CharacterVersion character) {
		return issues.stream().map(issue -> new CharacterAppearance(issue, character)).toList();
	}

	public void removeUnlistedAppearances(List<Issue> characterAppearances, List<Issue> recordedAppearances,
			CharacterVersion character) {
		/*
		 * if an issue is listed in recordedAppearances but not in characterAppearances,
		 * it means the character is no longer listed as a featured character in an
		 * issue. but if recordedAppearances is empty, it means there were no
		 * appearances recorded in the database prior to this update
		 */
		if (recordedAppearances.isEmpty() || AppConfig.DEBUG_MODE) {
			return;
		}
		List<CharacterAppearance> unlistedAppearances = recordedAppearances.stream()
				.filter(recordedAppearance -> !characterAppearances.contains(recordedAppearance))
				.map(issue -> new CharacterAppearance(issue, character)).toList();
		appearanceRepository.deleteAll(unlistedAppearances);
	}
}
