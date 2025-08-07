package com.santos.ravenapi.search.util;

import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public class QueryFormatter {

	public static int BUFFER = 500;

	public synchronized static String characterAppearances(PublisherEndpointEnum publisher, String character) {
		return String.format(
				"%s?action=query&format=json&formatversion=2&list=categorymembers&cmlimit=%d&cmtitle=Category%3A%s/Appearances",
				publisher.getUrl(), BUFFER, character);
	}

	public synchronized static String characterAppearancesPagination(PublisherEndpointEnum publisher, String character,
			String nextPageCode) {
		return new StringBuilder(characterAppearances(publisher, character))
				.append(String.format("&continue=-||&cmcontinue=%s", nextPageCode)).toString();
	}

	public synchronized static String issueDetails(PublisherEndpointEnum publisher, String issueId) {
		return String.format("%s?action=parse&format=json&formatversion=2&pageid=%s", publisher.getUrl(), issueId);
	}

	public synchronized static String disambiguationPage(PublisherEndpointEnum publisher, String characterAlias) {
		return String.format("%s?action=query&prop=revisions&rvprop=content&format=json&titles=%s", publisher.getUrl(),
				characterAlias);
	}
}
