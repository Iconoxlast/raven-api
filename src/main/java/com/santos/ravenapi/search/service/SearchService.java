package com.santos.ravenapi.search.service;

import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface SearchService {

	OutputDTO getCharacterAppearances(PublisherEnum publisher, String character);
}
