package com.santos.ravenapi.search.service;

import java.util.Optional;

import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface SearchService {

	OutputDTO getCharacterData(PublisherEnum publisher, String character);
	
	Optional<OutputDTO> getCharacterAppearances(PublisherEnum publisher, String character);
	
	Optional<OutputDTO> getCharacterDisambiguation(PublisherEnum publisher, String character);
}
