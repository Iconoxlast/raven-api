package com.santos.ravenapi.infra.exception;

import java.sql.SQLException;

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
		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Type", "error").body(ResponseBodyBuilder.error(
				HttpStatus.NOT_FOUND.value(), "Not Found",
				"Character not found. Verify that the character's name or version is correct and matches the informed publisher."));

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> treatInvalidPublisherException(MethodArgumentTypeMismatchException e) {
		return ResponseEntity.badRequest().header("Type", "error")
				.body(ResponseBodyBuilder.error(HttpStatus.BAD_REQUEST.value(), "Bad Request",
						String.format("Invalid or unavailable publisher. Details: %s", e.getMessage())));
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<String> treatSQLException(SQLException e) {
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Type", "error")
				.body(ResponseBodyBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
						String.format("Database error. Details: %s", e.getMessage())));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> treatNullPointerException(NullPointerException e) {
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Type", "error")
				.body(ResponseBodyBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
						String.format("Application error. Details: %s", e.getMessage())));
	}
}
