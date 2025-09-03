package com.santos.ravenapi.search.client.query;

import com.santos.ravenapi.model.dto.disambiguation.query.QueryActionDTO;

public class DcQueryStrategy extends PublisherQueryStrategy {

	public DcQueryStrategy() {
		super();
		endpoint = "https://dc.fandom.com/api.php";
	}

	@Override
	public String disambiguationPage(String characterAlias) {
		return new StringBuilder(endpoint).append("?action=query&prop=revisions&rvprop=content&format=json&titles=")
				.append(characterAlias).toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<QueryActionDTO> getDisambiguationQueryClass() {
		return QueryActionDTO.class;
	}

}
