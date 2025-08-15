package com.santos.ravenapi.persistence.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.infra.config.AppConfig;
import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.model.jpa.Issue;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.IssueRepository;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class IssueServiceImpl implements IssueService {

	@Autowired
	private IssueRepository issueRepository;

	public List<Issue> saveNewIssues(List<Issue> characterAppearances, List<Issue> recordedAppearances) {
		/*
		 * if an issue is listed in characterAppearances but not in recordedAppearances,
		 * it means there are new appearances to be added to the character's appearance
		 * list
		 */
		List<Issue> issuesIncluded = recordedAppearances.isEmpty() ? characterAppearances
				: characterAppearances.stream().filter(appearance -> !recordedAppearances.contains(appearance))
						.toList();
		if (!AppConfig.DEBUG_MODE) {
			issueRepository.saveAll(issuesIncluded);
		}
		return issuesIncluded;
	}

	public List<Issue> convertDtoListToEntityList(Publisher publisher, List<IssueOutput> issuesDto) {
		return issuesDto.stream()
				.map(issueDto -> new Issue(issueDto.id(), publisher, issueDto.title(), issueDto.getLocalDate()))
				.toList();
	}

	public List<IssueOutput> convertEntityListToDtoList(List<Issue> issues) {
		return issues.stream().map(issue -> new IssueOutput(YearMonth.from(issue.getIssPublicationDate()),
				issue.getIssPageName(), issue.getIssPageId())).toList();
	}

	public List<Issue> getRecordedCharacterAppearances(PublisherEnum publisher, Long cverId) {
		return issueRepository.findAllByCharacterAppearances(publisher.getId(), cverId);
	}

	public List<Issue> getUpToDateAppearanceIssues(PublisherEnum publisher, String characterVersion,
			int lastUpdateLimit) {
		return issueRepository.findAllByCharacterVersionWithinInterval(publisher.getId(), characterVersion,
				LocalDateTime.now().minusHours(lastUpdateLimit));
	}

}
