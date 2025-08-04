package com.santos.ravenapi.infra.exception;

public class InvalidPublisherException extends RuntimeException {
	private static final long serialVersionUID = -2541490914649925070L;

	public InvalidPublisherException() {
		super("Invalid or unavailable publisher");
	}
}
