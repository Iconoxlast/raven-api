package com.santos.ravenapi.persistence.service;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santos.ravenapi.model.jpa.Publisher;
import com.santos.ravenapi.model.repository.PublisherRepository;
import com.santos.ravenapi.search.enums.PublisherEnum;

@Service
public class PublisherServiceImpl implements PublisherService {
	
	@Autowired
	private PublisherRepository publisherRepository;

	public Publisher getPublisherRecord(PublisherEnum publisherEnum) throws SQLException {
		Optional<Publisher> publisher = publisherRepository.findById(publisherEnum.getId());
		if (publisher.isEmpty()) {
			throw new SQLException("Publisher %s's record not found in the database.", publisherEnum.toString());
		}
		return publisher.get();
	}
}
