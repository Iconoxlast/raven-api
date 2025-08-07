package com.santos.ravenapi.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.dto.search.appearances.CategoryMember;
import com.santos.ravenapi.model.dto.search.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.search.issues.Category;
import com.santos.ravenapi.model.dto.search.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.model.vo.IssueDetailsVO;
import com.santos.ravenapi.search.client.FandomApiClient;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;
import com.santos.ravenapi.search.service.parser.FandomDataParser;

@Service
public class FandomQueryServiceImpl implements FandomQueryService {

	@Autowired
	private FandomApiClient apiClient;

	public List<IssueDetailsVO> getAppearances(PublisherEndpointEnum endpoint, String character) {
		List<IssueDetailsVO> appearancesList = new ArrayList<>();
		FandomAppearancesDTO appearancesDto = apiClient.queryAppearances(endpoint, character);
		addAppearancesToList(appearancesList, appearancesDto.query().categorymembers());
		if (appearancesDto.cont() != null) {
			do {
				appearancesDto = apiClient.queryAppearances(endpoint, character, appearancesDto.cont().cmcontinue());
				addAppearancesToList(appearancesList, appearancesDto.query().categorymembers());
			} while (appearancesDto.cont() != null);
		}
		addPublicationDates(endpoint, appearancesList);
		return appearancesList;
	}

	private void addAppearancesToList(List<IssueDetailsVO> appearancesList, List<CategoryMember> categoryMembers) {
		categoryMembers.forEach(categoryMember -> appearancesList
				.add(new IssueDetailsVO(categoryMember.title(), categoryMember.pageid())));
	}

	/**
	 * Assigns a publication date to each issue by querying issue details through an
	 * API request. Generally an issue's article on a comic book fandom has the
	 * issue's publication date as one of the article's categories, so the most
	 * reliable way to get the publication date is by sifting through the
	 * categories. This method filters out categories that are likely usable
	 * publication dates (see Category's isCategoryIssueDate() method) and
	 * prioritizes the most detailed (ex.: "2017, August" over "2017"). Some
	 * articles may not have a publication month or year listed, and in that case,
	 * the YearMonth "1900-01" is assigned to them for later treatment.
	 * 
	 * @param appearancesList
	 */
	private void addPublicationDates(PublisherEndpointEnum endpoint, List<IssueDetailsVO> appearancesList) {
		appearancesList.forEach(issue -> {
			FandomIssueDetailsDTO issueDto = apiClient.queryIssueDetails(endpoint, issue.getId());
			Optional<Category> optCategory = issueDto.parse().categories().stream()
					.filter(Category::isCategoryIssueDate)
					.sorted((cat1, cat2) -> Integer.compare(cat2.category().length(), cat1.category().length()))
					.findFirst();
			issue.setDate(FandomDataParser.getYearMonth(optCategory.isPresent() ? optCategory.get().category() : ""));
		});
	}
}
