package com.santos.ravenapi.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.santos.ravenapi.search.util.ResponseBodyBuilder;

@RestControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler(CharacterNotFoundException.class)
	public ResponseEntity<String> treatCharacterNotFoundException(CharacterNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseBodyBuilder.error(404,
				"Not Found",
				"Character not found. Verify that the character's name or version is correct and matches the informed publisher."));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> treatInvalidPublisherException(MethodArgumentTypeMismatchException e) {
		return ResponseEntity.badRequest().body(ResponseBodyBuilder.error(400, "Bad Request",
				String.format("Invalid or unavailable publisher. Details: %s", e.getMessage())));
	}
}
