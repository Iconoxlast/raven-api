package com.santos.ravenapi.search.client.query;

public class MarvelQueryStrategy extends PublisherQueryStrategy {

	public MarvelQueryStrategy() {
		super();
		endpoint = "https://marvel.fandom.com/api.php";
	}

	// TODO to be reviewed; this doesn't quite work for Marvel disambiguation queries
	@Override
	public String disambiguationPage(String characterAlias) {
		return new StringBuilder(endpoint).append("?action=query&prop=revisions&rvprop=content&format=json&titles=")
				.append(characterAlias).toString();
	}

}
