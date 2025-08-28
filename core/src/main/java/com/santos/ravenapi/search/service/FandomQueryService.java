package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.appearances.CategoryMember;
import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.issues.Category;
import com.santos.ravenapi.model.dto.issues.FandomIssueDetailsDTO;
import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.persistence.service.AppearanceService;
import com.santos.ravenapi.persistence.service.DisambiguationService;
import com.santos.ravenapi.search.client.FandomApiClient;
import com.santos.ravenapi.search.client.query.PublisherQueryStrategy;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.parser.FandomDateParser;

public abstract class FandomQueryService {

	@Autowired
	protected FandomApiClient apiClient;
	@Autowired
	protected AppearanceService appearanceService;
	@Autowired
	protected DisambiguationService disambiguationService;
	protected PublisherQueryStrategy queryStrategy;
	protected PublisherEnum publisher;
	protected final int batchSize = 50;

	/**
	 * Fetches a particular character's listed appearances and their publication
	 * dates through the FandomApiClient, and returns a list of DTOs corresponding
	 * to the listed issues.
	 * 
	 * @param character
	 * @return Optional<List<IssueOutput>>
	 * @throws SQLException
	 */
	public Optional<List<IssueOutput>> getAppearances(String character) throws SQLException {
		ArgumentValidator.validate(character);
		List<IssueOutput> appearancesList = queryAppearances(character);
		return appearancesList.isEmpty() ? Optional.empty() : Optional.of(appearancesList);
	}

	protected List<IssueOutput> queryAppearances(String character) throws SQLException {
		List<IssueOutput> appearancesList = new ArrayList<>();
		FandomAppearancesDTO appearancesDto = null;
		do {
			appearancesDto = appearancesDto == null ? apiClient.queryAppearances(queryStrategy, character)
					: apiClient.queryAppearances(queryStrategy, character, appearancesDto.cont().cmcontinue());
			addAppearancesToList(appearancesList, appearancesDto.query().categorymembers());
		} while (appearancesDto.cont() != null);
		sortAppearancesByPublicationDate(appearancesList);
		appearanceService.updateCharacterAppearances(publisher, character, appearancesList);
		return appearancesList;
	}

	protected void addAppearancesToList(List<IssueOutput> appearancesList,
			List<CategoryMember> categoryMembers) {
		Map<Long, IssueDataVO> issuesData = new TreeMap<>();

		List<List<Long>> batches = partitionIdList(categoryMembers.stream().map(CategoryMember::pageid).toList());

		batches.forEach(batch -> {
			processIssuesBatchData(batch, issuesData);
		});
		appearancesList.addAll(issuesData.entrySet().stream()
				.map(issueData -> new IssueOutput(
						issueData.getValue().dateIsSet() ? issueData.getValue().publicationDate : YearMonth.of(1900, 1),
						issueData.getValue().title, issueData.getKey()))
				.toList());
	}

	protected void processIssuesBatchData(List<Long> batch,
			Map<Long, IssueDataVO> issuesData) {
		FandomIssueDetailsDTO issuesDto = null;
		do {
			issuesDto = issuesDto == null ? issuesDto = apiClient.queryIssueDetails(queryStrategy, batch)
					: apiClient.queryIssueDetails(queryStrategy, batch, issuesDto.cont().clcontinue());
			issuesDto.query().pages().forEach(page -> {
				if (page.categories() != null) {
					issuesData.putIfAbsent(page.pageid(), new IssueDataVO(page.title()));
					IssueDataVO issueData = issuesData.get(page.pageid());
					if (!issueData.dateIsSet()) {
						Optional<Category> optCategory = page.categories().stream()
								.filter(Category::isCategoryIssueDate).sorted((cat1, cat2) -> Integer
										.compare(cat2.getCategory().length(), cat1.getCategory().length()))
								.findFirst();
						if (optCategory.isPresent()) {
							issueData.publicationDate = FandomDateParser.getYearMonth(optCategory.get().getCategory());
						}
					}
				}
			});
		} while (issuesDto.cont() != null);
	}

	protected List<List<Long>> partitionIdList(List<Long> pageIds) {
		return IntStream.range(0, (pageIds.size() + batchSize - 1) / batchSize)
				.mapToObj(i -> pageIds.subList(i * batchSize, Math.min((i + 1) * batchSize, pageIds.size())))
				.collect(Collectors.toList());
	}

	protected void sortAppearancesByPublicationDate(List<IssueOutput> appearancesList) {
		if (appearancesList.isEmpty()) {
			return;
		}
		appearancesList.sort(Comparator.comparing(IssueOutput::date));
	}

	/**
	 * Fetches a list of character versions associated with a particular character
	 * alias through the FandomApiClient, returning a DTO containing the listed
	 * character versions.
	 * 
	 * @param character
	 * @return Optional<DisambiguationOutput>
	 * @throws SQLException 
	 */
	abstract public Optional<DisambiguationOutput> getDisambiguation(String character) throws SQLException;

	protected class IssueDataVO {
		final String title;
		YearMonth publicationDate;

		public IssueDataVO(String title) {
			this.title = title;
		}

		public boolean dateIsSet() {
			return publicationDate != null;
		}
	}
}
