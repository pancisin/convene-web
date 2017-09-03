package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.models.enums.SurveyState;

@Entity
@Table(name = "surveys")
public class Survey {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;

	@Column
	private Date start_date;

	@Column
	private Date end_date;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	private List<MetaField> metaFields;

	@JsonIgnore
	@OneToMany(mappedBy = "survey")
	private List<SurveySubmission> submissions;

	@Enumerated(EnumType.STRING)
	@JsonProperty(access = Access.READ_ONLY)
	private SurveyState state = SurveyState.NEW;

	public void addMetaField(MetaField field) {
		if (this.metaFields == null)
			this.metaFields = new ArrayList<MetaField>();

		this.metaFields.add(field);
	}

	public String getName() {
		return name;
	}
	
	public int getSubmissionsCount() {
		if (this.submissions != null) 
			return this.submissions.size();
		return 0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Long getId() {
		return id;
	}

	public void setMetaFields(List<MetaField> metaFields) {
		if (this.metaFields == null)
			this.metaFields = new ArrayList<MetaField>();

		this.metaFields.clear();

		if (metaFields != null)
			this.metaFields.addAll(metaFields);
	}

	public List<MetaField> getMetaFields() {
		return metaFields;
	}

	public List<SurveySubmission> getSubmissions() {
		return submissions;
	}

	public SurveyState getState() {
		return state;
	}

	public void setState(SurveyState state) {
		this.state = state;
	}
}
