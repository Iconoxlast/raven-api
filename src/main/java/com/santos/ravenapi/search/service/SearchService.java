package com.santos.ravenapi.search.service;

import com.santos.ravenapi.model.dto.search.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface SearchService {

	OutputDTO getCharacterData(PublisherEnum publisher, String character);
	
	OutputDTO getCharacterAppearances(PublisherEnum publisher, String character);
	
	DisambiguationOutput getCharacterDisambiguation(PublisherEnum endpoint, String character);
}
