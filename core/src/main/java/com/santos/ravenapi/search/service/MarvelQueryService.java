package com.santos.ravenapi.search.service;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.dto.output.DisambiguationOutput;
import com.santos.ravenapi.search.client.query.MarvelQueryStrategy;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service("MARVEL")
public class MarvelQueryService extends FandomQueryService {

	public MarvelQueryService() {
		super();
		super.publisher = PublisherEnum.MARVEL;
		super.queryStrategy = new MarvelQueryStrategy();
	}

	@Override
	public Optional<DisambiguationOutput> getDisambiguation(String character) throws SQLException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

}
