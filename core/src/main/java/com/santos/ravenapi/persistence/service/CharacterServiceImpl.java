package com.santos.ravenapi.persistence.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.config.AppConfig;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.jpa.Character;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.CharacterRepository;

@Service
public class CharacterServiceImpl implements CharacterService {

	@Autowired
	private CharacterRepository characterRepository;

	public void saveNewCharacterAliases(Publisher publisher, List<String> characterAliases) {
		if (AppConfig.DEBUG_MODE) {
			return;
		}
		ArgumentValidator.validate(publisher, characterAliases);
		List<String> newCharacterAliases = new ArrayList<>();
		List<String> existingRecords = characterRepository
				.findByCharPublisher_PublIdAndCharPageNameIn(publisher.getPublId(), characterAliases).get().stream()
				.map(Character::getCharPageName).toList();
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
				.map(alias -> new Character(null, publisher, alias, LocalDateTime.of(1900, 1, 1, 0, 0))).toList());
	}

	public void updateCharacterLastUpdate(Character character) {
		if (AppConfig.DEBUG_MODE) {
			return;
		}
		ArgumentValidator.validate(character);
		character.setCharLatestUpdate(LocalDateTime.now());
		characterRepository.save(character);
	}

	public Optional<Character> getCharacterVersionByPageName(Long publisherId, String pageName) {
		return characterRepository.findByCharPublisher_PublIdAndCharPageName(publisherId, pageName);
	}
}
