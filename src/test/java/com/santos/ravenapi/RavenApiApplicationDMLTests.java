package com.santos.ravenapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.santos.ravenapi.model.dto.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.SearchService;

@SpringBootTest
@ActiveProfiles("test")
public class RavenApiApplicationDMLTests {

	@Autowired
	private SearchService service;
	
	@Test
	void getCharacterDisambiguationFromDatabaseTest() {
		Optional<OutputDTO> output = null;
		try {
			output = service.getCharacterDisambiguation(PublisherEnum.DC, "Noah Kuttler");			
		} catch (Exception e) {
			System.out.println(e.getClass());
		}
		assertThat(output).isPresent();
	}
}
