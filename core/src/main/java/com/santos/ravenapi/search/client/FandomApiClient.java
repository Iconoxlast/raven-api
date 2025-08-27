package com.santos.ravenapi.search.client;

import java.util.List;

import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.client.query.PublisherQueryStrategy;

public interface FandomApiClient {

	FandomAppearancesDTO queryAppearances(PublisherQueryStrategy queryStrategy, String character);

	FandomAppearancesDTO queryAppearances(PublisherQueryStrategy queryStrategy, String character, String cont);

	FandomIssueDetailsDTO queryIssueDetails(PublisherQueryStrategy queryStrategy, List<Long> issueIds);

	FandomIssueDetailsDTO queryIssueDetails(PublisherQueryStrategy queryStrategy, List<Long> issueIds, String cont);

	FandomDisambiguationDTO queryDisambiguation(PublisherQueryStrategy queryStrategy, String character);
}
