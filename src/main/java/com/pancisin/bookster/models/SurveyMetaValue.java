package com.pancisin.bookster.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name = "surveys_meta_values")
public class SurveyMetaValue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JsonSerialize(using = ToStringSerializer.class)
	private User user;
	
	@JsonIgnore
	@ManyToOne
	private Survey survey;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private MetaValue meta_value;

	public SurveyMetaValue() {
		
	}
	
	public SurveyMetaValue(User user, Survey survey, MetaValue value) {
		this.user = user;
		this.survey = survey;
		this.meta_value = value;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public MetaValue getMeta_value() {
		return meta_value;
	}

	public void setMeta_value(MetaValue meta_value) {
		this.meta_value = meta_value;
	}

	public Long getId() {
		return id;
	}
}
