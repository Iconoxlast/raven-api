package com.santos.ravenapi.search.service;

import java.util.List;

import com.santos.ravenapi.model.dto.search.output.IssueOutput;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface FandomQueryService {

	/**
	 * Requests a list of a particular character's appearances and each appearance's
	 * publication date through the FandomApiClient, and returns a list of DTOs
	 * corresponding to the listed issues.
	 * 
	 * @param publisher
	 * @param character
	 * @return List<IssueOutput>
	 */
	List<IssueOutput> getAppearances(PublisherEnum publisher, String character);

}
