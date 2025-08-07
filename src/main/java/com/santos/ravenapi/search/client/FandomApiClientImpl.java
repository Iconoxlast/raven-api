package com.santos.ravenapi.search.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.search.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;
import com.santos.ravenapi.search.util.QueryFormatter;

@Component
public class FandomApiClientImpl implements FandomApiClient {
	
	private RestTemplate restTemplate;

	@Override
	public FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character) {
		return queryAppearances(endpoint, character, "");
	}

	@Override
	public FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character, String cont) {
		if (cont.isEmpty()) {
			return restTemplate.getForObject(QueryFormatter.characterAppearances(endpoint, character),
					FandomAppearancesDTO.class);
		}
		return restTemplate.getForObject(
				QueryFormatter.characterAppearancesPagination(endpoint, character, cont),
				FandomAppearancesDTO.class);
	}

	public FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, int issueId) {
		return restTemplate.getForObject(QueryFormatter.issueDetails(endpoint, issueId + ""),
				FandomIssueDetailsDTO.class);
	}

	public FandomDisambiguationDTO queryDisambiguation(PublisherEndpointEnum publisher, String character) {
		return restTemplate.getForObject(QueryFormatter.disambiguationPage(publisher, character),
				FandomDisambiguationDTO.class);
	}

}
