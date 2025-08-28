package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.exception.DisambiguationPageNotFoundException;
import com.santos.ravenapi.infra.validation.ArgumentValidator;
import com.santos.ravenapi.model.dto.disambiguation.FandomDisambiguationDTO;
import com.santos.ravenapi.model.dto.disambiguation.Page;
import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.search.client.query.DcQueryStrategy;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.util.DisambiguationTextFilter;

@Service("DC")
public class DcQueryService extends FandomQueryService {

	public DcQueryService() {
		super();
		super.publisher = PublisherEnum.DC;
		super.queryStrategy = new DcQueryStrategy();
	}

	public Optional<DisambiguationOutput> getDisambiguation(String character)
			throws SQLException {
		ArgumentValidator.validate(publisher, character);
		DisambiguationOutput output = null;
		try {
			List<String> characterAliases = new ArrayList<>();
			List<String> characterVersions = new ArrayList<>();
			boolean isRedirectPage = false;
			do {
				FandomDisambiguationDTO disambiguationDto = apiClient.queryDisambiguation(queryStrategy,
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
//			Collections.sort(characterVersions);
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
}
