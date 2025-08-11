package com.santos.ravenapi.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.AppearancesNotFoundException;
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
		character = character.replace(" ", "_");
		try {
			return getCharacterAppearances(publisher, character);
		} catch (AppearancesNotFoundException e) {
			try {
				return getCharacterDisambiguation(publisher, character);
			} catch (DisambiguationPageNotFoundException e2) {
				throw new CharacterNotFoundException();
			}
		}
	}

	public AppearancesOutput getCharacterAppearances(PublisherEnum publisher, String character)
			throws AppearancesNotFoundException {
		// TODO data persistence to be implemented; it should make a database query
		// before sending a request to the external API
		List<IssueOutput> issueDetails = queryService.getAppearances(publisher, character);
		if (issueDetails.isEmpty()) {
			throw new AppearancesNotFoundException();
		}
		return new AppearancesOutput(issueDetails.stream().collect(Collectors.groupingBy(IssueOutput::date)));
	}

	public DisambiguationOutput getCharacterDisambiguation(PublisherEnum endpoint, String character)
			throws DisambiguationPageNotFoundException {
		// TODO data persistence to be implemented; it should make a database query
		// before sending a request to the external API

		// TODO getDisambiguation() method in the FandomQueryService class
		return null;
	}
}
