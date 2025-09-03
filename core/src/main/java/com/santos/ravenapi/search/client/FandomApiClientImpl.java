package com.santos.ravenapi.search.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.search.client.query.PublisherQueryStrategy;

@Component
public class FandomApiClientImpl implements FandomApiClient {

	@Autowired
	private RestTemplate restTemplate;

	public FandomAppearancesDTO queryAppearances(PublisherQueryStrategy queryStrategy, String character) {
		return queryAppearances(queryStrategy, character, "");
	}

	public FandomAppearancesDTO queryAppearances(PublisherQueryStrategy queryStrategy, String character, String cont) {
		ArgumentValidator.validate(queryStrategy, character, cont);
		if (cont.isEmpty()) {
			return restTemplate.getForObject(queryStrategy.characterAppearances(character), FandomAppearancesDTO.class);
		}
		return restTemplate.getForObject(queryStrategy.characterAppearancesPagination(character, cont),
				FandomAppearancesDTO.class);
	}

	public FandomIssueDetailsDTO queryIssueDetails(PublisherQueryStrategy queryStrategy, List<Long> issueIds) {
		return queryIssueDetails(queryStrategy, issueIds, "");
	}

	public FandomIssueDetailsDTO queryIssueDetails(PublisherQueryStrategy queryStrategy, List<Long> issueIds,
			String cont) {
		ArgumentValidator.validate(queryStrategy, issueIds, cont);
		if (cont.isEmpty()) {
			return restTemplate.getForObject(queryStrategy.issueDetails(issueIds), FandomIssueDetailsDTO.class);
		}
		return restTemplate.getForObject(queryStrategy.issueDetailsPagination(issueIds, cont),
				FandomIssueDetailsDTO.class);
	}

	public FandomDisambiguationDTO queryDisambiguation(PublisherQueryStrategy queryStrategy, String character) {
		ArgumentValidator.validate(queryStrategy, character);
		return restTemplate.getForObject(queryStrategy.disambiguationPage(character),
				queryStrategy.getDisambiguationQueryClass());
	}

}
