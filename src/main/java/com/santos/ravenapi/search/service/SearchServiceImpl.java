package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.CharacterNotFoundException;
import com.santos.ravenapi.model.dto.output.AppearancesOutput;
import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.model.dto.output.OutputDTO;
import com.santos.ravenapi.persistence.service.AppearanceService;
import com.santos.ravenapi.persistence.service.DisambiguationService;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private FandomQueryService queryService;
	@Autowired
	private AppearanceService appearanceService;
	@Autowired
	private DisambiguationService disambiguationService;
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

	public OutputDTO getCharacterData(PublisherEnum publisher, String character) {
		// orElseGet() instead of orElse() because orElse() will try both conditions.
		// orElseGet is a lazy evaluation
		logger.info(String.format("Character data query: %s, %s, %s", publisher, character,
				LocalDateTime.now().format(dateFormatter)));
		return getCharacterAppearances(publisher, character).orElseGet(
				() -> getCharacterDisambiguation(publisher, character).orElseThrow(CharacterNotFoundException::new));
	}

	public Optional<OutputDTO> getCharacterAppearances(PublisherEnum publisher, String character) {
		// it must make a database query before sending a request to the external API
		Optional<List<IssueOutput>> appearancesList = null;
		try {
			appearancesList = appearanceService.getAppearancesDTO(publisher, character);
			if (appearancesList.isEmpty()) {
				logger.info(String.format("Character appearances data not found in database. Updating data. %s, %s",
						publisher, character));
				appearancesList = queryService.getAppearances(publisher, character);
				logger.info(appearancesList.isEmpty()
						? String.format("Character appearances not found in external API. %s, %s", publisher, character)
								: String.format("Character appearances retrieved from external API. Data updated. %s, %s, %s",
										publisher, character, LocalDateTime.now().format(dateFormatter)));
			} else {
				logger.info(String.format("Character appearances data retrieved from database. %s, %s, %s", publisher,
						character, LocalDateTime.now().format(dateFormatter)));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			appearancesList = Optional.empty();
		}

		// Divides the list by their publication dates, turning the dates into keys on a
		// Map. TreeMap used to ensure the keys are ordered chronologically
		return Optional
				.ofNullable(appearancesList.isPresent()
						? new AppearancesOutput(appearancesList.get().stream()
								.collect(Collectors.groupingBy(IssueOutput::date, TreeMap::new, Collectors.toList())))
						: null);
	}

	public Optional<OutputDTO> getCharacterDisambiguation(PublisherEnum publisher, String character) {
		Optional<DisambiguationOutput> disambiguation = null;
		try {
			disambiguation = disambiguationService.getDisambiguationDTO(publisher,
					character);
			if (disambiguation.isEmpty()) {
				logger.info(String.format("Character disambiguation data not found in database. Updating data. %s, %s",
						publisher, character));
				disambiguation = queryService.getDisambiguation(publisher, character);
				logger.info(disambiguation.isEmpty()
						? String.format("Character disambiguation data not found in external API. %s, %s", publisher,
								character)
								: String.format(
										"Character disambiguation data retrieved from external API. Data updated. %s, %s, %s",
										publisher, character, LocalDateTime.now().format(dateFormatter)));
			} else {
				logger.info(String.format("Character disambiguation data retrieved from database. %s, %s, %s", publisher,
						character, LocalDateTime.now().format(dateFormatter)));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			disambiguation = Optional.empty();
		}
		// TODO data persistence to be implemented; it should make a database query
		// before sending a request to the external API

		return Optional.ofNullable(disambiguation.isPresent() ? disambiguation.get() : null);
	}
}
