package com.santos.ravenapi.search.service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.dto.search.appearances.CategoryMember;
import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.issues.Category;
import com.santos.ravenapi.model.dto.search.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.model.dto.search.output.IssueOutput;
import com.santos.ravenapi.search.client.FandomApiClient;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.parser.FandomDataParser;

@Service
public class FandomQueryServiceImpl implements FandomQueryService {

	@Autowired
	private FandomApiClient apiClient;

	public List<IssueOutput> getAppearances(PublisherEnum publisher, String character) {
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
	private YearMonth getPublicationDate(PublisherEnum publisher, int pageId) {
		FandomIssueDetailsDTO issueDto = apiClient.queryIssueDetails(publisher.getEndpoint(), pageId);
		Optional<Category> optCategory = issueDto.parse().categories().stream().filter(Category::isCategoryIssueDate)
				.sorted((cat1, cat2) -> Integer.compare(cat2.category().length(), cat1.category().length()))
				.findFirst();
		return FandomDataParser.getYearMonth(optCategory.isPresent() ? optCategory.get().category() : "");
	}

	private void sortAppearancesByPublicationDate(List<IssueOutput> appearancesList) {
		appearancesList.sort(Comparator.comparing(IssueOutput::date));
	}
}
