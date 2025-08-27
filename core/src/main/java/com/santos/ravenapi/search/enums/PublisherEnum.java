package com.santos.ravenapi.search.enums;

import com.santos.ravenapi.search.client.query.DcQueryStrategy;
import com.santos.ravenapi.search.client.query.MarvelQueryStrategy;
import com.santos.ravenapi.search.client.query.PublisherQueryStrategy;

/**
 * Enum that not only lists the publishers available for queries, but their
 * respective IDs in the database's "publishers" table.
 */
public enum PublisherEnum {

	DC(1, new DcQueryStrategy()), MARVEL(2, new MarvelQueryStrategy());

	private long id;
	private PublisherQueryStrategy strategy;

	PublisherEnum(long id, PublisherQueryStrategy strategy) {
		this.id = id;
		this.strategy = strategy;
	}

	public long getId() {
		return id;
	}

	public PublisherQueryStrategy getQuery() {
		return strategy;
	}
}
