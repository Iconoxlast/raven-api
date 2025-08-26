package com.santos.ravenapi.persistence.service;

import java.util.List;

import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.model.jpa.Issue;
import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface IssueService {

	List<Issue> saveNewIssues(List<Issue> characterAppearances, List<Issue> recordedAppearances);
	
	List<Issue> convertDtoListToEntityList(Publisher publisher, List<IssueOutput> issuesDto);
	
	List<IssueOutput> convertEntityListToDtoList(List<Issue> issues);
	
	List<Issue> getRecordedCharacterAppearances(PublisherEnum publisher, Long cverId);
	
	List<Issue> getUpToDateAppearanceIssues(PublisherEnum publisher, String characterVersion, int lastUpdateLimit);
}
