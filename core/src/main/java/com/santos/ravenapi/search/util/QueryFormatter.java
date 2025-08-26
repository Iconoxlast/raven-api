package com.santos.ravenapi.search.util;

import java.util.List;

import com.santos.ravenapi.infra.config.AppConfig;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public class QueryFormatter {

	public synchronized static String characterAppearances(PublisherEndpointEnum publisher, String character) {
		return new StringBuilder(publisher.getUrl())
				.append("?action=query&format=json&formatversion=2&list=categorymembers&cmlimit=").append(AppConfig.BUFFER_SIZE)
				.append("&cmtitle=Category:").append(character).append("/Appearances").toString();
	}

	public synchronized static String characterAppearancesPagination(PublisherEndpointEnum publisher, String character,
			String nextPageCode) {
		return new StringBuilder(characterAppearances(publisher, character)).append("&continue=-||&cmcontinue=")
				.append(nextPageCode).toString();
	}

	public synchronized static String issueDetails(PublisherEndpointEnum publisher, List<Long> issueIds) {
		StringBuilder ids = new StringBuilder();
		issueIds.forEach(issueId -> {
			if (ids.length() > 0) {
				ids.append("|");
			}
			ids.append(issueId);			
		});
		return new StringBuilder(publisher.getUrl()).append("?action=query&prop=categories&format=json&formatversion=2&cllimit=500&pageids=")
				.append(ids).toString();
	}
	
	public synchronized static String issueDetailsPagination(PublisherEndpointEnum publisher, List<Long> issueIds, String nextPageCode) {
		return new StringBuilder(issueDetails(publisher, issueIds)).append("&clcontinue=").append(nextPageCode).toString();
	}

	public synchronized static String disambiguationPage(PublisherEndpointEnum publisher, String characterAlias) {
		return new StringBuilder(publisher.getUrl())
				.append("?action=query&prop=revisions&rvprop=content&format=json&titles=").append(characterAlias)
				.toString();
	}
}
