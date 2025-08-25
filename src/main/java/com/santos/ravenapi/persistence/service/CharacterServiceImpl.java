package com.santos.ravenapi.persistence.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.jpa.Character;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.CharacterRepository;

@Service
public class CharacterServiceImpl implements CharacterService {
	
	@Autowired
	private CharacterRepository characterRepository;

	public void saveNewCharacterAliases(Publisher publisher, List<String> characterAliases) {
		List<String> newCharacterAliases = new ArrayList<>();
		List<String> existingRecords = characterRepository.findByCharPublIdAndCharPageNameIn(publisher.getPublId(), characterAliases).get().stream()
				.map(alias -> alias.getCharPageName()).toList();
		if (existingRecords.isEmpty()) {
			newCharacterAliases.addAll(characterAliases);
		} else {
			newCharacterAliases = characterAliases.stream().filter(version -> !existingRecords.contains(version))
					.toList();
		}
		if (newCharacterAliases.isEmpty()) {
			return;
		}
		characterRepository.saveAllAndFlush(newCharacterAliases.stream()
				.map(alias -> new Character(null, publisher, alias, LocalDateTime.of(1900, 1, 1, 0, 0)))
				.toList());
	}
}
