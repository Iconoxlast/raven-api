package com.santos.ravenapi.search.client.query;

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

}
