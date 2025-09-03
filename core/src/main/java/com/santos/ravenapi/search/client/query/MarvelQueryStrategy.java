package com.santos.ravenapi.search.client.query;

import com.santos.ravenapi.model.dto.disambiguation.parse.ParseActionDTO;

public class MarvelQueryStrategy extends PublisherQueryStrategy {

	public MarvelQueryStrategy() {
		super();
		endpoint = "https://marvel.fandom.com/api.php";
	}

	// TODO to be reviewed; this doesn't quite work for Marvel disambiguation queries
	@Override
	public String disambiguationPage(String characterAlias) {
		return new StringBuilder(endpoint).append("?action=parse&prop=text&format=json&page=")
				.append(characterAlias).toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ParseActionDTO> getDisambiguationQueryClass() {
		return ParseActionDTO.class;
	}

}
