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
 * Entity that represents the version of a character in a particular reality
 * from a publisher's multiverse, or versions from different media (movies,
 * games, etc). The distinction is necessary for a number of reasons, chief
 * among them the fact that an issue only features particular versions of the
 * characters playing a part in the story(ies).
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "character_versions")
@Entity(name = "CharacterVersion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "cverId")
public class CharacterVersion {

	@Id
	@Column(name = "cver_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cverId;
	@ManyToOne
	@JoinColumn(name = "cver_publ_id")
	private Publisher cverPublisher;
	@Column(name = "cver_page_name")
	private String cverPageName;
	@Column(name = "cver_latest_update")
	private LocalDateTime cverLatestUpdate;
}
