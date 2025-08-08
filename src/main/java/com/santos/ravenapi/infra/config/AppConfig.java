package com.santos.ravenapi.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
public class AppConfig {
	
	public static int BUFFER_SIZE;
	
	@Value("${api.pagination.buffer.size}")
	public void setBufferSize(int buffer) {
		BUFFER_SIZE = buffer;
	}
	
}
