package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private List<SurveyMetaValue> metaValues;
	
	public void addMetaField(MetaField field) {
		if (this.metaFields == null)
			this.metaFields = new ArrayList<MetaField>();

		this.metaFields.add(field);
	}

	public String getName() {
		return name;
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

	public List<SurveyMetaValue> getMetaValues() {
		return metaValues;
	}
}
