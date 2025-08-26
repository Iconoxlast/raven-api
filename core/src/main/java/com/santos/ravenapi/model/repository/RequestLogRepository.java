package com.santos.ravenapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.ravenapi.model.jpa.RequestLog;

/**
 * Repository for access to the "request_log" table. It allows for the inclusion
 * and search of request logs, records on each request made to this API.
 * Services are not supposed to delete any of these records by any means.
 * 
 * @author Joao Paulo Santos
 */
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

	default void delete(RequestLog requestLog) {
		throw new UnsupportedOperationException();
	}
	
	default void deleteById(Long id) {
		throw new UnsupportedOperationException();
	}
	
	default void deleteAll() {
		throw new UnsupportedOperationException();
	}
}
