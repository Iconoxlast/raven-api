package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.model.dto.appearances.CategoryMember;
import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.disambiguation.Page;
import com.santos.ravenapi.model.dto.issues.Category;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.persistence.service.AppearanceService;
import com.santos.ravenapi.search.client.FandomApiClient;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.parser.FandomDataParser;
import com.santos.ravenapi.search.util.DisambiguationTextFilter;

@Service
public class FandomQueryServiceImpl implements FandomQueryService {

	@Autowired
	private FandomApiClient apiClient;
	@Autowired
	private AppearanceService appearanceService;

	public Optional<List<IssueOutput>> getAppearances(PublisherEnum publisher, String character) throws SQLException {
		Optional<List<IssueOutput>> appearancesList = appearanceService.getCharacterAppearancesDTO(publisher,
				character);
		if (appearancesList.isPresent() && !appearancesList.get().isEmpty()) {
			return appearancesList;
		}
		appearancesList = Optional.of(queryAppearances(publisher, character));
		return appearancesList.get().isEmpty() ? Optional.empty() : appearancesList;
	}

	private List<IssueOutput> queryAppearances(PublisherEnum publisher, String character) throws SQLException {
		List<IssueOutput> appearancesList = new ArrayList<>();
		FandomAppearancesDTO appearancesDto = apiClient.queryAppearances(publisher.getEndpoint(), character);
		addAppearancesToList(publisher, appearancesList, appearancesDto.query().categorymembers());
		if (appearancesDto.cont() != null) {
			do {
				appearancesDto = apiClient.queryAppearances(publisher.getEndpoint(), character,
						appearancesDto.cont().cmcontinue());
				addAppearancesToList(publisher, appearancesList, appearancesDto.query().categorymembers());
			} while (appearancesDto.cont() != null);
		}
		sortAppearancesByPublicationDate(appearancesList);
		appearanceService.updateCharacterAppearances(publisher, character, appearancesList);
		return appearancesList;
	}

	private void addAppearancesToList(PublisherEnum endpoint, List<IssueOutput> appearancesList,
			List<CategoryMember> categoryMembers) {
		categoryMembers.forEach(categoryMember -> appearancesList
				.add(new IssueOutput(getPublicationDate(endpoint, categoryMember.pageid()), categoryMember.title(),
						categoryMember.pageid())));
	}

	/**
	 * Assigns a publication date to an issue by querying issue details through an
	 * API request. Generally an issue's article on a comic book fandom has the
	 * issue's publication date as one of the article's categories, so the most
	 * reliable way to get the publication date is by sifting through the
	 * categories. This method filters out categories that are likely usable
	 * publication dates (see Category's isCategoryIssueDate() method) and
	 * prioritizes the most detailed (ex.: "2017, August" over "2017"). Some
	 * articles may not have a publication month or year listed, and in that case,
	 * the YearMonth "1900-01" is assigned to them for later treatment.
	 * 
	 * @param publisher
	 * @param pageId
	 */
	private YearMonth getPublicationDate(PublisherEnum publisher, long pageId) {
		FandomIssueDetailsDTO issueDto = apiClient.queryIssueDetails(publisher.getEndpoint(), pageId);
		Optional<Category> optCategory = issueDto.parse().categories().stream().filter(Category::isCategoryIssueDate)
				.sorted((cat1, cat2) -> Integer.compare(cat2.category().length(), cat1.category().length()))
				.findFirst();
		return FandomDataParser.getYearMonth(optCategory.isPresent() ? optCategory.get().category() : "");
	}

	private void sortAppearancesByPublicationDate(List<IssueOutput> appearancesList) {
		if (appearancesList.isEmpty()) {
			return;
		}
		appearancesList.sort(Comparator.comparing(IssueOutput::date));
	}

	public Optional<DisambiguationOutput> getDisambiguation(PublisherEnum publisher, String character) {
		DisambiguationOutput output = null;
		try {
			List<String> characterAliases = new ArrayList<>();
			List<String> characterVersions = new ArrayList<>();
			FandomDisambiguationDTO disambiguationDto = null;
			boolean isRedirectPage = false;
			do {
				disambiguationDto = apiClient.queryDisambiguation(publisher.getEndpoint(), character);
				validateQueriedPage(disambiguationDto.query().pages());
				Page page = disambiguationDto.query().pages()
						.get(disambiguationDto.query().pages().keySet().toArray()[0]);
				characterAliases.add(page.title());
				String revisionContent = page.revisions().get(0).content();
				isRedirectPage = revisionContent.contains("#REDIRECT");
				if (isRedirectPage) {
					character = DisambiguationTextFilter.filterRedirect(revisionContent);
					continue;
				}
				validateQueriedPageContent(revisionContent);
				characterVersions.addAll(DisambiguationTextFilter.filterCharacterNames(revisionContent));
			} while (isRedirectPage);
			// TODO persist data
			Collections.sort(characterVersions);
			output = new DisambiguationOutput(characterVersions);
		} catch (DisambiguationPageNotFoundException e) {
			System.err.println("Character not found"); // TODO temporary
		}
		return Optional.ofNullable(output);
	}

	private void validateQueriedPage(Map<String, Page> pages) {
		// -1 key indicates no pages with the searched title were found on the wiki
		if (pages.get("-1") != null) {
			throw new DisambiguationPageNotFoundException();
		}
	}

	private void validateQueriedPageContent(String content) {
		// character disambiguation articles generally start with "{{Disambig\n"
		if (!content.split("\\|")[0].contains("Disambig")) {
			throw new DisambiguationPageNotFoundException();
		}
	}
}
