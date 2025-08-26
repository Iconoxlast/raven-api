package com.santos.ravenapi.model.jpa;

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
 * Entity that represents a publisher in the database. It should be noted that a
 * record's "publ_id" value should correspond to the values listed in
 * PublisherEnum.
 * 
 * @author Joao Paulo Santos
 */
@Table(name = "publishers")
@Entity(name = "Publisher")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "publId")
public class Publisher {

	@Id
	@Column(name = "publ_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long publId;
	@Column(name = "publ_name")
	private String publName;
}
