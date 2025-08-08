package com.santos.ravenapi.search.marvel.service;

import com.santos.ravenapi.infra.exception.AppearancesNotFoundException;
import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.model.dto.search.output.AppearancesOutput;
import com.santos.ravenapi.model.dto.search.output.DisambiguationOutput;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.PublisherSearchService;

public class MarvelSearchServiceImpl implements PublisherSearchService {

	@Override
	public AppearancesOutput getCharacterData(String character) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppearancesOutput getCharacterAppearances(PublisherEnum publisher, String character)
			throws AppearancesNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DisambiguationOutput getCharacterDisambiguation(PublisherEnum publisher, String character)
			throws DisambiguationPageNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
