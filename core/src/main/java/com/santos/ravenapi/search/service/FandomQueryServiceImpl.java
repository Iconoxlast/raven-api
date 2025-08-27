package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.appearances.CategoryMember;
import com.santos.ravenapi.model.dto.appearances.FandomAppearancesDTO;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.disambiguation.Page;
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
import com.santos.ravenapi.search.util.DisambiguationTextFilter;

@Service
public class FandomQueryServiceImpl implements FandomQueryService {

	@Autowired
	private FandomApiClient apiClient;
	@Autowired
	private AppearanceService appearanceService;
	@Autowired
	private DisambiguationService disambiguationService;
	private final int batchSize = 50;

	public Optional<List<IssueOutput>> getAppearances(PublisherEnum publisher, String character) throws SQLException {
		ArgumentValidator.validate(publisher, character);
		List<IssueOutput> appearancesList = queryAppearances(publisher, character);
		return appearancesList.isEmpty() ? Optional.empty() : Optional.of(appearancesList);
	}

	private List<IssueOutput> queryAppearances(PublisherEnum publisher, String character) throws SQLException {
		List<IssueOutput> appearancesList = new ArrayList<>();
		FandomAppearancesDTO appearancesDto = null;
		do {
			appearancesDto = appearancesDto == null ? apiClient.queryAppearances(publisher.getQuery(), character)
					: apiClient.queryAppearances(publisher.getQuery(), character, appearancesDto.cont().cmcontinue());
			addAppearancesToList(publisher, appearancesList, appearancesDto.query().categorymembers());
		} while (appearancesDto.cont() != null);
		sortAppearancesByPublicationDate(appearancesList);
		appearanceService.updateCharacterAppearances(publisher, character, appearancesList);
		return appearancesList;
	}

	private void addAppearancesToList(PublisherEnum publisher, List<IssueOutput> appearancesList,
			List<CategoryMember> categoryMembers) {
		Map<Long, IssueDataVO> issuesData = new TreeMap<>();

		List<List<Long>> batches = partitionIdList(categoryMembers.stream().map(CategoryMember::pageid).toList());

		batches.forEach(batch -> {
			processIssuesBatchData(publisher.getQuery(), batch, issuesData);
		});
		appearancesList.addAll(issuesData.entrySet().stream()
				.map(issueData -> new IssueOutput(
						issueData.getValue().dateIsSet() ? issueData.getValue().publicationDate : YearMonth.of(1900, 1),
						issueData.getValue().title, issueData.getKey()))
				.toList());
	}

	private void processIssuesBatchData(PublisherQueryStrategy queryStrategy, List<Long> batch,
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

	private List<List<Long>> partitionIdList(List<Long> pageIds) {
		return IntStream.range(0, (pageIds.size() + batchSize - 1) / batchSize)
				.mapToObj(i -> pageIds.subList(i * batchSize, Math.min((i + 1) * batchSize, pageIds.size())))
				.collect(Collectors.toList());
	}

	private void sortAppearancesByPublicationDate(List<IssueOutput> appearancesList) {
		if (appearancesList.isEmpty()) {
			return;
		}
		appearancesList.sort(Comparator.comparing(IssueOutput::date));
	}

	public Optional<DisambiguationOutput> getDisambiguation(PublisherEnum publisher, String character)
			throws SQLException {
		ArgumentValidator.validate(publisher, character);
		DisambiguationOutput output = null;
		try {
			List<String> characterAliases = new ArrayList<>();
			List<String> characterVersions = new ArrayList<>();
			boolean isRedirectPage = false;
			do {
				FandomDisambiguationDTO disambiguationDto = apiClient.queryDisambiguation(publisher.getQuery(),
						character);
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
			if (characterVersions.isEmpty()) {
				throw new DisambiguationPageNotFoundException();
			}
			Collections.sort(characterVersions);
			disambiguationService.updateDisambiguationData(publisher, characterAliases, characterVersions);
			output = new DisambiguationOutput(characterVersions);
		} catch (DisambiguationPageNotFoundException e) {
			return Optional.empty();
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

	private class IssueDataVO {
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
