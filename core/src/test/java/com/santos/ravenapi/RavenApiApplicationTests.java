package com.santos.ravenapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.santos.ravenapi.model.dto.output.IssueOutput;
import com.santos.ravenapi.model.dto.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.FandomServiceRegistry;
import com.santos.ravenapi.search.service.SearchService;

@SpringBootTest
@ActiveProfiles("debug")
class RavenApiApplicationTests {

	@Autowired
	private SearchService searchService;
	@Autowired
	private FandomServiceRegistry serviceRegistry;

	@Test
	void throwCharacterNotFoundExceptionTest() {
		OutputDTO output = null;
		try {
			output = searchService.getCharacterData(PublisherEnum.DC, "asdasda");
		} catch (Exception e) {
			System.out.println(e.getClass());
		}
		assertThat(output).isNull();
	}

	@Test
	void getCalculatorAppearancesTest() {
		Optional<List<IssueOutput>> appearances = Optional.empty();
		try {
			appearances = getCharacterAppearances("Noah Kuttler (Prime Earth)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(appearances).isNotEmpty();
		assertThat(appearances.get()).isNotEmpty();
	}

	@Test
	void getRavenAppearancesTest() {
		Optional<List<IssueOutput>> appearances = Optional.empty();
		try {
			appearances = getCharacterAppearances("Raven (Prime Earth)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(appearances).isNotEmpty();
		assertThat(appearances.get()).isNotEmpty();
	}

	private Optional<List<IssueOutput>> getCharacterAppearances(String character) {
		long startTime = Instant.now().toEpochMilli();
		Optional<List<IssueOutput>> appearances = Optional.empty();
		try {
			appearances = serviceRegistry.getService(PublisherEnum.DC).getAppearances(character);
			appearances.get().forEach(issue -> System.out.printf("%s - %s\r\n", issue.date(), issue.title()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.printf("%.4f s\r\n", (double) (Instant.now().toEpochMilli() - startTime) / 1000);
		return appearances;
	}

	@Test
	void getCharacterDisambiguationsFromDcFandomTest() {
		Optional<OutputDTO> output = null;
		try {
			output = searchService.getCharacterDisambiguation(PublisherEnum.DC, "Noah Kuttler");
		} catch (Exception e) {
			System.out.println(e.getClass());
		}
		assertThat(output).isPresent();
	}

	@Test
	void getCharacterDisambiguationsFromMarvelFandomTest() {
		Optional<OutputDTO> output = null;
		try {
			output = searchService.getCharacterDisambiguation(PublisherEnum.MARVEL, "Mania");
		} catch (Exception e) {
			System.out.println(e.getClass());
		}
		assertThat(output).isPresent();
	}
}
