package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.util.Optional;

import com.santos.ravenapi.model.dto.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface SearchService {

	OutputDTO getCharacterData(PublisherEnum publisher, String character) throws SQLException;
	
	Optional<OutputDTO> getCharacterAppearances(PublisherEnum publisher, String character) throws SQLException;
	
	Optional<OutputDTO> getCharacterDisambiguation(PublisherEnum publisher, String character);
}
