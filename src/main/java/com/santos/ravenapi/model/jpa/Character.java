package com.santos.ravenapi.model.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
 * Entity that represents a character's alias. It may be either their real name
 * or an identity that may be shared between multiple characters. The publisher
 * must be identified, as the same alias may be shared between multiple
 * publishers (ex. both DC and Marvel have a Captain Marvel). This entity is
 * employed in case the queried term in the client's GET request leads to a
 * disambiguation page.
 * 
 * Data regarding these aliases needs to be updated periodically, hence why
 * there's a "latest update" datetime attribute.
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "characters")
@Entity(name = "Character")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "charId")
public class Character {

	@Id
	@Column(name = "char_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long charId;
	@ManyToOne
	@JoinColumn(name = "char_publ_id")
	private Publisher charPublisher;
	@Column(name = "char_page_name")
	private String charPageName;
	@Column(name = "char_latest_update")
	private LocalDateTime charLatestUpdate;

}
