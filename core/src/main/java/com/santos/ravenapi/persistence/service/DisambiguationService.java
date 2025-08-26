package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface DisambiguationService {

	Optional<DisambiguationOutput> getDisambiguationDTO(PublisherEnum publisher, String character);

	void updateDisambiguationData(PublisherEnum publisher, List<String> characterAliases,
			List<String> characterVersions) throws SQLException;

}
