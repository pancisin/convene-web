package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name = "surveys_submissions", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "survey_id" }) })
public class SurveySubmission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JsonProperty(access = Access.READ_ONLY)
	@JsonSerialize(using = ToStringSerializer.class)
	private User user;

	@JsonIgnore
	@ManyToOne
	private Survey survey;

	@OneToMany(cascade = CascadeType.ALL)
	private List<MetaValue> values;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	public SurveySubmission() {
		
	}
	
	public SurveySubmission(User user, Survey survey) {
		this.user = user;
		this.survey = survey;
	}
	
	public User getUser() {
		return user;
	}

	public Survey getSurvey() {
		return survey;
	}

	public List<MetaValue> getValues() {
		return values;
	}

	public void setValues(List<MetaValue> values) {
		this.values = values;
	}

	public Long getId() {
		return id;
	}

	public Calendar getCreated() {
		return created;
	}
}
