package com.santos.ravenapi.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler(CharacterNotFoundException.class)
	public ResponseEntity<String> treatCharacterNotFoundException(CharacterNotFoundException e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<String> treatInvalidPublisherException(InvalidFormatException e) {
		return ResponseEntity.badRequest()
				.body(String.format("Invalid or unavailable publisher. Details: %s", e.getMessage()));
	}
}
