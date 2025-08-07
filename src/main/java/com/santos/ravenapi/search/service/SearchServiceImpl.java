package com.santos.ravenapi.search.service;

import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.InvalidPublisherException;
import com.santos.ravenapi.model.dto.search.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class SearchServiceImpl implements SearchService {

	private PublisherSearchService dcService;
	private PublisherSearchService marvelService;

	public OutputDTO getCharacterAppearances(PublisherEnum publisher, String character) {
		switch (publisher) {
		case DC:
			return dcService.getCharacterData(character);
		case MARVEL:
			return marvelService.getCharacterData(character);
		default:
			throw new InvalidPublisherException();
		}
	}
}
