package com.santos.ravenapi.model.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity that represents the table in which all the requests made to this API
 * will be logged for security reasons and performance evaluations. It includes
 * the request method, the URI in the query, the client's IP, the HTTP response
 * status, the client's locale, the amount of time it took for the application
 * to process the request in milliseconds, and the time when the request was
 * made.
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "request_log")
@Entity(name = "RequestLog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "reqsId")
public class RequestLog {

	@Id
	@Column(name = "reqs_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reqsId;
	@Column(name = "reqs_method")
	private String reqsMethod;
	@Column(name = "reqs_uri")
	private String reqsUri;
	@Column(name = "reqs_client_ip")
	private String reqsClientIp;
	@Column(name = "reqs_status")
	private Integer reqsStatus;
	@Column(name = "reqs_user_agent")
	private String reqsUserAgent;
	@Column(name = "reqs_locale")
	private String reqsLocale;
	@Column(name = "reqs_response_time")
	private Long reqsResponseTime;
	@Column(name = "reqs_timestamp")
	private LocalDateTime reqsTimestamp;
}
