package com.santos.ravenapi.search.client.query;

import java.util.List;

import com.santos.ravenapi.infra.config.AppConfig;

public abstract class PublisherQueryStrategy {

	protected String endpoint;

	public String characterAppearances(String character) {
		return new StringBuilder(endpoint)
				.append("?action=query&format=json&formatversion=2&list=categorymembers&cmlimit=")
				.append(AppConfig.BUFFER_SIZE).append("&cmtitle=Category:").append(character).append("/Appearances")
				.toString();
	}

	public String characterAppearancesPagination(String character, String nextPageCode) {
		return new StringBuilder(characterAppearances(character)).append("&continue=-||&cmcontinue=")
				.append(nextPageCode).toString();
	}

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

	public String issueDetailsPagination(List<Long> issueIds, String nextPageCode) {
		return new StringBuilder(issueDetails(issueIds)).append("&clcontinue=").append(nextPageCode).toString();
	}

	abstract public String disambiguationPage(String characterAlias);
}
