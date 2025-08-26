package com.santos.ravenapi.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.santos.ravenapi.infra.interceptor.LoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private LoggingInterceptor ipInterceptor;
	
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ipInterceptor).addPathPatterns("/**");
	}

}
