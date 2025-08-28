package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.CharacterNotFoundException;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
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
	private AppearanceService appearanceService;
	@Autowired
	private DisambiguationService disambiguationService;
	@Autowired
	private FandomServiceRegistry serviceRegistry;
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

	public OutputDTO getCharacterData(PublisherEnum publisher, String character) {
		ArgumentValidator.validate(publisher, character);
		// orElseGet() instead of orElse() because orElse() will try both conditions.
		// orElseGet is a lazy evaluation
		logger.info(String.format("Character data query: %s, %s, %s", publisher, character,
				LocalDateTime.now().format(dateFormatter)));
		return getCharacterAppearances(publisher, character).orElseGet(
				() -> getCharacterDisambiguation(publisher, character).orElseThrow(CharacterNotFoundException::new));
	}

	public Optional<OutputDTO> getCharacterAppearances(PublisherEnum publisher, String character) {
		ArgumentValidator.validate(publisher, character);
		// it must make a database query before sending a request to the external API
		List<IssueOutput> appearancesList = null;
		try {
			appearancesList = appearanceService.getAppearancesDTO(publisher, character).get();
			logger.info(String.format("Character appearances data retrieved from database. %s, %s, %s", publisher,
					character, LocalDateTime.now().format(dateFormatter)));
		} catch (NoSuchElementException e) {
			try {
				logger.info(String.format("Character appearances data not found in database. Updating data. %s, %s",
						publisher, character));
				appearancesList = serviceRegistry.getService(publisher).getAppearances(character).get();
				logger.info(String.format("Character appearances retrieved from external API. Data updated. %s, %s, %s",
						publisher, character, LocalDateTime.now().format(dateFormatter)));
			} catch (NoSuchElementException e2) {
				logger.info(
						String.format("Character appearances not found in external API. %s, %s", publisher, character));
			} catch (SQLException e2) {
				logger.error(e.getMessage());
			}
		}
		//
		return getAppearancesOutput(appearancesList);
	}

	/**
	 * Recollects the list breaking it down by their publication dates, turning the
	 * dates into keys on a Map. TreeMap used to ensure the keys are ordered
	 * chronologically.
	 * 
	 * @param appearancesList
	 * @return Optional<OutputDTO>
	 */
	private Optional<OutputDTO> getAppearancesOutput(List<IssueOutput> appearancesList) {
		if (appearancesList == null) {
			return Optional.empty();
		}
		return Optional.of(new AppearancesOutput(appearancesList.stream()
				.collect(Collectors.groupingBy(IssueOutput::date, TreeMap::new, Collectors.toList()))));
	}

	public Optional<OutputDTO> getCharacterDisambiguation(PublisherEnum publisher, String character) {
		ArgumentValidator.validate(publisher, character);
		DisambiguationOutput disambiguation = null;
		try {
			disambiguation = disambiguationService.getDisambiguationDTO(publisher, character).get();
			logger.info(String.format("Character disambiguation data retrieved from database. %s, %s, %s", publisher,
					character, LocalDateTime.now().format(dateFormatter)));
		} catch (NoSuchElementException e) {
			try {
				logger.info(String.format("Character disambiguation data not found in database. Updating data. %s, %s",
						publisher, character));
				disambiguation = serviceRegistry.getService(publisher).getDisambiguation(character).get();
				logger.info(String.format(
						"Character disambiguation data retrieved from external API. Data updated. %s, %s, %s",
						publisher, character, LocalDateTime.now().format(dateFormatter)));
			} catch (NoSuchElementException e2) {
				logger.info(String.format("Character disambiguation data not found in external API. %s, %s", publisher,
						character));
			} catch (SQLException e2) {
				logger.error(e.getMessage());
			}
		}
		return Optional.ofNullable(disambiguation);
	}
}
