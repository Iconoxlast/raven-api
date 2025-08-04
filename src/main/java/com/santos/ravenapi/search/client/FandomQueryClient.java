package com.santos.ravenapi.search.client;

import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public interface FandomQueryClient {

	FandomAppearancesDTO queryAppearances(PublisherEndpointEnum publisher, String query);

	FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum publisher, String query);

}
