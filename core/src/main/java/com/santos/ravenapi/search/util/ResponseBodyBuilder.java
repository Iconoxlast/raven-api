package com.santos.ravenapi.search.util;

import java.time.LocalDateTime;

public class ResponseBodyBuilder {

	public synchronized static String error(int status, String error, String message) {
		return String.format("""
				{
					"timestamp": "%s",
					"status": %d,
					"error": "%s",
					"message": "%s"
				}""", LocalDateTime.now().toString(), status, error, message);
	}
}
