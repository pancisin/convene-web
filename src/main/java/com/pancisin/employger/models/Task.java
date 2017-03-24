package com.pancisin.employger.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String section;

	@Column
	private String fixture;
	
	@Column
	private String action;

	@JsonIgnore
	@ManyToOne
	private Duty duty;
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getFixture() {
		return fixture;
	}

	public void setFixture(String fixture) {
		this.fixture = fixture;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
