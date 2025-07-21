package com.santos.ravenapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

	@GetMapping
	public void searchForCharacter(@RequestBody String json) {
		System.out.println(json);
	}
}
