package com.santos.ravenapi.search.client;

import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public interface FandomApiClient {

	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character);
	
	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character, String cont);
	
	FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, int issueId);
}
