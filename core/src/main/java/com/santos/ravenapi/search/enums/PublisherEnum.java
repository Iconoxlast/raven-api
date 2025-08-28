package com.santos.ravenapi.search.enums;

/**
 * Enum that not only lists the publishers available for queries, but their
 * respective IDs in the database's "publishers" table.
 */
public enum PublisherEnum {

	DC(1), MARVEL(2);

	private long id;

	PublisherEnum(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}
