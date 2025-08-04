package com.santos.ravenapi.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler(CharacterNotFoundException.class)
	public ResponseEntity<String> treatCharacterNotFoundException(CharacterNotFoundException e) {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(InvalidPublisherException.class)
	public ResponseEntity<String> treatInvalidPublisherException(InvalidPublisherException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
