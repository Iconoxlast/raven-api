package com.santos.ravenapi.search.client.query;

import java.util.List;

public interface PublisherQueryStrategy {

	String characterAppearances(String character);

	String characterAppearancesPagination(String character, String nextPageCode);

	String issueDetails(List<Long> issueIds);

	String issueDetailsPagination(List<Long> issueIds, String nextPageCode);

	String disambiguationPage(String characterAlias);

}
