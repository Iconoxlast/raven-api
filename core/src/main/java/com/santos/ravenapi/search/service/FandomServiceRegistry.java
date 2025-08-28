package com.santos.ravenapi.search.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.santos.ravenapi.search.enums.PublisherEnum;

@Component
public class FandomServiceRegistry {

	private Map<String, FandomQueryService> services;
	
	public FandomServiceRegistry(Map<String, FandomQueryService> services) {
		this.services = services;
	}

	public FandomQueryService getService(PublisherEnum publisher) {
		return services.get(publisher.toString());
	}
	
}
