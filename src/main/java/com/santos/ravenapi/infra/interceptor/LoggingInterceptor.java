package com.santos.ravenapi.infra.interceptor;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.santos.ravenapi.model.jpa.RequestLog;
import com.santos.ravenapi.model.repository.RequestLogRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

	@Autowired
	private RequestLogRepository logRepository;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute("startTime", System.currentTimeMillis());
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		String ip = request.getHeader("X-Forwarded-For").replace("X-Forwarded-For:", "").trim();
		String method = request.getMethod();
		String uri = request.getRequestURI();
		int responseStatus = response.getStatus();
		String userAgent = request.getHeader("User-Agent");
		String locale = request.getLocale().toString();
		long responseTime = System.currentTimeMillis() - (long) request.getAttribute("startTime");
		RequestLog log = new RequestLog(null, method, uri, ip, responseStatus, userAgent, locale, responseTime, LocalDateTime.now());
		// this is where it should be saved
//		logRepository.save(log);
		System.out.println(log);
	}

}
