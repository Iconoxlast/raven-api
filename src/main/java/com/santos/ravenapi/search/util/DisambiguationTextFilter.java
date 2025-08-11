package com.santos.ravenapi.search.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisambiguationTextFilter {

	private static Pattern patMainPage = Pattern.compile("\\|\\s*MainPage\\s*=\\s*(.*?)\\n\\|");
	private static Pattern patThumbnav = Pattern.compile("\\{\\{thumbnav\\|(.*?)(\\||})");
	
	public synchronized static String filterRedirect(String text) {
		return text.replaceAll("#REDIRECT\\[\\[(.*?)]]", "$1");
	}
	
	public synchronized static Set<String> filterCharacterNames(String content) {
		Set<String> characters = new HashSet<>();
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
}
