package com.santos.ravenapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.santos.ravenapi.search.enums.PublisherEnum;
import com.santos.ravenapi.search.service.SearchService;

@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService service;

	@GetMapping
	public ResponseEntity<?> getCharacterAppearances(@RequestParam PublisherEnum publisher,
			@RequestParam String character) {
		return ResponseEntity.ok(service.getCharacterAppearances(publisher, character));
	}
}
