package com.santos.ravenapi.search.util;

import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public class QueryFormatter {

	public static int BUFFER = 500;

	public synchronized static String characterAppearances(PublisherEndpointEnum publisher, String character) {
		return new StringBuilder(publisher.getUrl())
				.append("?action=query&format=json&formatversion=2&list=categorymembers&cmlimit=").append(BUFFER)
				.append("&cmtitle=Category%3A").append(character).append("/Appearances").toString();
	}

	public synchronized static String characterAppearancesPagination(PublisherEndpointEnum publisher, String character,
			String nextPageCode) {
		return new StringBuilder(characterAppearances(publisher, character)).append("&continue=-||&cmcontinue=")
				.append(nextPageCode).toString();
	}

	public synchronized static String issueDetails(PublisherEndpointEnum publisher, String issueId) {
		return new StringBuilder(publisher.getUrl()).append("?action=parse&format=json&formatversion=2&pageid=")
				.append(issueId).toString();
	}

	public synchronized static String disambiguationPage(PublisherEndpointEnum publisher, String characterAlias) {
		return new StringBuilder(publisher.getUrl())
				.append("?action=query&prop=revisions&rvprop=content&format=json&titles=").append(characterAlias)
				.toString();
	}
}
