package com.santos.ravenapi.search.util;

public class CharacterNormalizer {

	public synchronized static String normalize(String characterName) {
		return characterName.replace("_", " ").trim();
	}
}
