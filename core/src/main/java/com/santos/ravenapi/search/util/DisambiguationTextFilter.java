package com.santos.ravenapi.search.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisambiguationTextFilter {

	private static Pattern patMainPage = Pattern.compile("\\|\\s*MainPage\\s*=\\s*(.*?)\\n\\|");
	private static Pattern patThumbnav = Pattern.compile("\\{\\{thumbnav\\|(.*?)(\\||})");
	private static Pattern patWikiPage = Pattern.compile("/wiki/([^\"]+)\"");
	
	public synchronized static String filterRedirect(String text) {
		return text.replaceAll("#REDIRECT\\[\\[(.*?)]]", "$1");
	}
	
	public synchronized static Set<String> filterQueryCharacterNames(String content) {
		Set<String> characters = new LinkedHashSet<>();
		Matcher mainPageMatcher = patMainPage.matcher(content);
		while (mainPageMatcher.find()) {
			characters.add(mainPageMatcher.group(1));
		}
		Matcher thumbNavMatcher = patThumbnav.matcher(content);
		while (thumbNavMatcher.find()) {
			characters.add(thumbNavMatcher.group(1));
		}
		return characters;
	}
	
	public synchronized static Set<String> filterParseCharacterNames(String content) {
		Set<String> characters = new LinkedHashSet<>();
		Matcher pageMatcher = patWikiPage.matcher(content);
		while (pageMatcher.find()) {
			characters.add(pageMatcher.group(1).replace("_", " "));
		}
		return characters;
	}
}
