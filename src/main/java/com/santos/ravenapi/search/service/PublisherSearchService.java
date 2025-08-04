package com.santos.ravenapi.search.service;

import com.santos.ravenapi.model.dto.search.output.OutputDTO;

public interface PublisherSearchService {

	OutputDTO getCharacterAppearances(String character);
}
