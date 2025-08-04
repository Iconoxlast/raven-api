package com.santos.ravenapi.search.dc.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.client.FandomQueryClient;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;
import com.santos.ravenapi.search.service.PublisherSearchService;

public class DcSearchServiceImpl implements PublisherSearchService {
	
	@Autowired
	private FandomQueryClient client;
	
	@Override
	public OutputDTO getCharacterAppearances(String character) {
		FandomAppearancesDTO appearanceList = client.queryAppearances(PublisherEndpointEnum.DC, character);
		/**
		 * TODO: implement basic search logic.
		 * if appearances are found, query for the details on each issue.
		 * if no appearances are found, query for a disambiguation page.
		 * if no disambiguation page is found, throw CharacterNotFoundException
		 */
		return null;
	}

}
