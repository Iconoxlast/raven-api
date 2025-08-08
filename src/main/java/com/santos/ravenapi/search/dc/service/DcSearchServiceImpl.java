package com.santos.ravenapi.search.dc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.model.vo.IssueDetailsVO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;
import com.santos.ravenapi.search.service.FandomQueryService;
import com.santos.ravenapi.search.service.PublisherSearchService;

@Service
public class DcSearchServiceImpl implements PublisherSearchService {
	
	@Autowired
	private FandomQueryService queryService;
	
	@Override
	public OutputDTO getCharacterData(String character) {
		// Temporary; data persistence will be implemented soon
		character = character.replace(" ", "_");
		List<IssueDetailsVO> issueDetails = queryService.getAppearances(PublisherEndpointEnum.DC, character);
		issueDetails.forEach(issue -> System.out.println(issue));
		/**
		 * TODO: implement basic search logic.
		 * if appearances are found, query for the details on each issue.
		 * if no appearances are found, query for a disambiguation page.
		 * if no disambiguation page is found, throw CharacterNotFoundException
		 */
		return null;
	}

}
