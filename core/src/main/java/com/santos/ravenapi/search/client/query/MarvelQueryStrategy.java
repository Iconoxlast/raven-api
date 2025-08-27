package com.santos.ravenapi.search.client.query;

import java.util.List;

import com.santos.ravenapi.infra.config.AppConfig;

public class MarvelQueryStrategy implements PublisherQueryStrategy {

	private final String endpoint = "https://marvel.fandom.com/api.php";

	@Override
	public String characterAppearances(String character) {
		return new StringBuilder(endpoint)
				.append("?action=query&format=json&formatversion=2&list=categorymembers&cmlimit=")
				.append(AppConfig.BUFFER_SIZE).append("&cmtitle=Category:").append(character).append("/Appearances")
				.toString();
	}

	@Override
	public String characterAppearancesPagination(String character, String nextPageCode) {
		return new StringBuilder(characterAppearances(character)).append("&continue=-||&cmcontinue=")
				.append(nextPageCode).toString();
	}

	@Override
	public String issueDetails(List<Long> issueIds) {
		StringBuilder ids = new StringBuilder();
		issueIds.forEach(issueId -> {
			if (ids.length() > 0) {
				ids.append("|");
			}
			ids.append(issueId);
		});
		return new StringBuilder(endpoint)
				.append("?action=query&prop=categories&format=json&formatversion=2&cllimit=500&pageids=").append(ids)
				.toString();
	}

	@Override
	public String issueDetailsPagination(List<Long> issueIds, String nextPageCode) {
		return new StringBuilder(issueDetails(issueIds)).append("&clcontinue=").append(nextPageCode).toString();
	}

	// TODO to be reviewed; this doesn't quite work for Marvel disambiguation queries
	@Override
	public String disambiguationPage(String characterAlias) {
		return new StringBuilder(endpoint).append("?action=query&prop=revisions&rvprop=content&format=json&titles=")
				.append(characterAlias).toString();
	}

}
