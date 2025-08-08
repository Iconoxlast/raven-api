package com.santos.ravenapi.search.service;

import com.santos.ravenapi.infra.exception.AppearancesNotFoundException;
import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.model.dto.search.output.AppearancesOutput;
import com.santos.ravenapi.model.dto.search.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface PublisherSearchService {

	OutputDTO getCharacterData(String character);

	AppearancesOutput getCharacterAppearances(PublisherEnum publisher, String character)
			throws AppearancesNotFoundException;

	DisambiguationOutput getCharacterDisambiguation(PublisherEnum publisher, String character)
			throws DisambiguationPageNotFoundException;
}
