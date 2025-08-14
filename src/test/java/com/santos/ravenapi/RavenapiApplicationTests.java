package com.santos.ravenapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.santos.ravenapi.model.dto.output.OutputDTO;
import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.SearchService;

@SpringBootTest
class RavenApiApplicationTests {

	@Autowired
	private SearchService service;

	@Test
	void returnsCharacterNotFoundException() {
		OutputDTO output = null;
		try {
			output = service.getCharacterData(PublisherEnum.DC, "asdasda");			
		} catch (Exception e) {
			System.out.println(e.getClass());
		}
		assertThat(output).isNull();
	}

}
