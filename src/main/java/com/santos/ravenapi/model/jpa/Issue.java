package com.santos.ravenapi.model.jpa;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity that represents data about a particular comic book issue that is
 * necessary in order to list a character's appearances chronologically. The ID
 * isn't automatically assigned on the record's insertion; it's actually the
 * value of the "pageid" attribute returned from a GET request to the Fandom
 * API.
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "issues")
@Entity(name = "Issue")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "issPageId")
public class Issue {

	@Id
	@Column(name = "iss_page_id")
	private Long issPageId;
	@ManyToOne
	@JoinColumn(name = "iss_publ_id")
	private Publisher issPublisher;
	@Column(name = "iss_page_name")
	private String issPageName;
	@Column(name = "iss_publication_date")
	private LocalDate issPublicationDate;
}
