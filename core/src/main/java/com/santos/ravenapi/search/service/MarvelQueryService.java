package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.disambiguation.parse.Parse;
import com.santos.ravenapi.model.dto.disambiguation.parse.ParseActionDTO;
import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.search.client.query.MarvelQueryStrategy;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.util.DisambiguationTextFilter;

@Service("MARVEL")
public class MarvelQueryService extends FandomQueryService {

	public MarvelQueryService() {
		super();
		super.publisher = PublisherEnum.MARVEL;
		super.queryStrategy = new MarvelQueryStrategy();
	}

	@Override
	public Optional<DisambiguationOutput> getDisambiguation(String character) throws SQLException {
		/*
		 * Marvel disambiguation queries should work differently from DC's. Because of
		 * the way the disambiguations are set up on their Fandom Wiki, there isn't a
		 * definitive way to discern which links are alternate character versions, so
		 * the best we can do is return a list of names that may or may not be an alias
		 * of the character the user is looking for, and that may or may not link to a
		 * proper character version. These other aliases will be saved as character
		 * versions, but the application will be able to discern whether it's an actual
		 * character version or not by the number of listed appearances, which should be
		 * zero for aliases.
		 */
		ArgumentValidator.validate(character);
		DisambiguationOutput output = null;
		try {
			List<String> characterAliases = new ArrayList<>();
			List<String> relatedPages = new ArrayList<>();
			boolean isRedirectPage = false;
			do {
				ParseActionDTO disambiguationDto = (ParseActionDTO) apiClient.queryDisambiguation(queryStrategy,
						character);
				validateParsedPage(disambiguationDto.parse());
				characterAliases.add(character);
				String content = disambiguationDto.parse().text().content();
				isRedirectPage = content.contains("class=\"redirect");
				if (isRedirectPage) {
					character = DisambiguationTextFilter.filterParseCharacterNames(content).toArray()[0].toString();
					continue;
				}
				Set<String> relatedCharacters = DisambiguationTextFilter.filterParseCharacterNames(content);
				relatedPages.addAll(character.startsWith("Earth-")
						? relatedCharacters.stream().filter(name -> name.startsWith("Earth-")).toList()
						: relatedCharacters.stream().filter(name -> !name.startsWith("Earth-")).toList());
			} while (isRedirectPage);
			if (relatedPages.isEmpty()) {
				throw new DisambiguationPageNotFoundException();
			}
			disambiguationService.updateDisambiguationData(publisher, characterAliases, relatedPages);
			output = new DisambiguationOutput(relatedPages);
		} catch (DisambiguationPageNotFoundException | NullPointerException e) {
			return Optional.empty();
		}
		return Optional.ofNullable(output);
	}

	private void validateParsedPage(Parse page) {
		if (page == null) {
			throw new DisambiguationPageNotFoundException();
		}
		String content = page.text().content();
		boolean isRedirectPage = content.contains("class=\"redirect");
		boolean isDisambiguation = content.contains("class=\"disambiguation");
		if (!isRedirectPage && !isDisambiguation) {
			throw new DisambiguationPageNotFoundException();
		}
	}
}
