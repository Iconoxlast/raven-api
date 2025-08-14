package com.santos.ravenapi.search.client;

import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public interface FandomApiClient {

	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character);
	
	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character, String cont);
	
	FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, long issueId);
	
	FandomDisambiguationDTO queryDisambiguation(PublisherEndpointEnum endpoint, String character);
}
