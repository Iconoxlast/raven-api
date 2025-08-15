package com.santos.ravenapi.search.client;

import java.util.List;

import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public interface FandomApiClient {

	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character);
	
	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character, String cont);
	
	FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, List<Long> issueIds);
	
	FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, List<Long> issueIds, String cont);
	
	FandomDisambiguationDTO queryDisambiguation(PublisherEndpointEnum endpoint, String character);
}
