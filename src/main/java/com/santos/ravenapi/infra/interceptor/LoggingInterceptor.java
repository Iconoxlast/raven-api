package com.santos.ravenapi.infra.interceptor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
		long requestStartTime = (Long) request.getAttribute("startTime");
		long responseTime = System.currentTimeMillis() - requestStartTime;
		String ip = getIp(request);
		String method = request.getMethod();
		String uri = request.getRequestURI()
				+ (request.getQueryString() == null ? "" : request.getQueryString().replace("%20", " "));
		int responseStatus = response.getStatus();
		String userAgent = request.getHeader("User-Agent");
		String locale = request.getLocale().toString();
		RequestLog log = new RequestLog(null, method, uri, ip, responseStatus, userAgent, locale, responseTime,
				LocalDateTime.ofInstant(Instant.ofEpochMilli(requestStartTime), ZoneId.systemDefault()));
		logRepository.save(log);
	}

	private String getIp(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		return forwarded != null && !forwarded.isEmpty() ? forwarded.replace("X-Forwarded-For:", "").trim()
				: request.getRemoteAddr() != null && !request.getRemoteAddr().isEmpty() ? request.getRemoteAddr() : "";
	}
}
