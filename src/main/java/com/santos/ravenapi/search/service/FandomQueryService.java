package com.santos.ravenapi.search.service;

import java.util.List;
import java.util.Optional;

import com.santos.ravenapi.model.dto.search.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.search.output.IssueOutput;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface FandomQueryService {

	/**
	 * Fetches a particular character's listed appearances and their publication
	 * dates through the FandomApiClient, and returns a list of DTOs corresponding
	 * to the listed issues.
	 * 
	 * @param publisher
	 * @param character
	 * @return Optional<List<IssueOutput>>
	 */
	Optional<List<IssueOutput>> getAppearances(PublisherEnum publisher, String character);

	/**
	 * Fetches a list of character versions associated with a particular character
	 * alias through the FandomApiClient, returning a DTO containing the listed
	 * character versions.
	 * 
	 * @param publisher
	 * @param character
	 * @return Optional<DisambiguationOutput>
	 */
	Optional<DisambiguationOutput> getDisambiguation(PublisherEnum publisher, String character);
}
