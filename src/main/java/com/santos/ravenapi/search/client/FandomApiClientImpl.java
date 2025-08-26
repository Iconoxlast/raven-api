package com.santos.ravenapi.search.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;
import com.santos.ravenapi.search.util.QueryFormatter;

@Component
public class FandomApiClientImpl implements FandomApiClient {

	@Autowired
	private RestTemplate restTemplate;

	public FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character) {
		return queryAppearances(endpoint, character, "");
	}

	public FandomAppearancesDTO queryAppearances(PublisherEndpointEnum endpoint, String character, String cont) {
		ArgumentValidator.validate(endpoint, character, cont);
		if (cont.isEmpty()) {
			return restTemplate.getForObject(QueryFormatter.characterAppearances(endpoint, character),
					FandomAppearancesDTO.class);
		}
		return restTemplate.getForObject(QueryFormatter.characterAppearancesPagination(endpoint, character, cont),
				FandomAppearancesDTO.class);
	}
	
	public FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, List<Long> issueIds) {
		return queryIssueDetails(endpoint, issueIds, "");
	}

	public FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum endpoint, List<Long> issueIds, String cont) {
		ArgumentValidator.validate(endpoint, issueIds, cont);
		if (cont.isEmpty()) {
			return restTemplate.getForObject(QueryFormatter.issueDetails(endpoint, issueIds),
					FandomIssueDetailsDTO.class);			
		}
		return restTemplate.getForObject(QueryFormatter.issueDetailsPagination(endpoint, issueIds, cont),
				FandomIssueDetailsDTO.class);			
	}

	public FandomDisambiguationDTO queryDisambiguation(PublisherEndpointEnum endpoint, String character) {
		ArgumentValidator.validate(endpoint, character);
		return restTemplate.getForObject(QueryFormatter.disambiguationPage(endpoint, character),
				FandomDisambiguationDTO.class);
	}

}
