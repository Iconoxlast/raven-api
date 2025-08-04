package com.santos.ravenapi.search.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.search.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

@Component
public class FandomQueryClientImpl implements FandomQueryClient {

	private RestTemplate restTemplate;

	public FandomAppearancesDTO queryAppearances(PublisherEndpointEnum publisher, String query) {
		return queryEndpoint(publisher, query, FandomAppearancesDTO.class);
	}
	
	public FandomIssueDetailsDTO queryIssueDetails(PublisherEndpointEnum publisher, String query) {
		return queryEndpoint(publisher, query, FandomIssueDetailsDTO.class);
	}
	
	public FandomDisambiguationDTO queryDisambiguation(PublisherEndpointEnum publisher, String query) {
		return queryEndpoint(publisher, query, FandomDisambiguationDTO.class);
	}
	
	private <T> T queryEndpoint(PublisherEndpointEnum publisher, String query, Class<T> dtoClass) {
		return restTemplate.getForObject(new StringBuilder(publisher.getUrl()).append(query).toString(), dtoClass);
	}
}
