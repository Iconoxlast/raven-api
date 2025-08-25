package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;

import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.search.enums.PublisherEnum;

public interface PublisherService {
	
	Publisher getPublisherRecord(PublisherEnum publisherEnum) throws SQLException;

}
