package com.santos.ravenapi.search.service;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.CharacterNotFoundException;
import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.model.dto.search.output.AppearancesOutput;
import com.santos.ravenapi.model.dto.search.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.search.output.IssueOutput;
import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private FandomQueryService queryService;

	public OutputDTO getCharacterData(PublisherEnum publisher, String character) {
		// orElseGet() instead of orElse() because orElse() will try both conditions.
		// orElseGet is a lazy evaluation
		return getCharacterAppearances(publisher, character).orElseGet(
				() -> getCharacterDisambiguation(publisher, character).orElseThrow(CharacterNotFoundException::new));
//		try {
//			return getCharacterAppearances(publisher, character);
//		} catch (AppearancesNotFoundException e) {
//			try {
//				return getCharacterDisambiguation(publisher, character);
//			} catch (DisambiguationPageNotFoundException e2) {
//				throw new CharacterNotFoundException();
//			}
//		}
	}

	public Optional<OutputDTO> getCharacterAppearances(PublisherEnum publisher, String character) {
		Optional<List<IssueOutput>> issueDetails = queryService.getAppearances(publisher, character);
		// TODO data persistence to be implemented; it should make a database query
		// before sending a request to the external API
		
		// Divides the list by their publication dates, turning the dates into keys on a
		// Map. TreeMap used to ensure the keys are ordered chronologically
		return Optional
				.ofNullable(issueDetails.isPresent()
						? new AppearancesOutput(issueDetails.get().stream()
								.collect(Collectors.groupingBy(IssueOutput::date, TreeMap::new, Collectors.toList())))
						: null);
	}

	public Optional<OutputDTO> getCharacterDisambiguation(PublisherEnum publisher, String character)
			throws DisambiguationPageNotFoundException {
		Optional<DisambiguationOutput> disambiguation = queryService.getDisambiguation(publisher, character);
		// TODO data persistence to be implemented; it should make a database query
		// before sending a request to the external API

		return Optional.ofNullable(disambiguation.isPresent() ? disambiguation.get() : null);
	}
}
