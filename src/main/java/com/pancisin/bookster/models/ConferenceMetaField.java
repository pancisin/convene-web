package com.pancisin.bookster.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.bookster.models.enums.MetaType;

@Entity
@Table(name = "conference_meta_fields")
public class ConferenceMetaField {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne(optional = false)
	private Conference conference;

	@Column
	private String name;

	@Enumerated(EnumType.STRING)
	private MetaType type;

	@Column
	private String description;

	@JsonIgnore
	@OneToMany(mappedBy = "field")
	private List<ConferenceMetaValue> values;

	@Column
	private String options;

	@Column
	private boolean optional;

	@Override
	public String toString() {
		return this.id.toString();
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public Long getId() {
		return id;
	}

	public MetaType getType() {
		return type;
	}

	public void setType(MetaType type) {
		this.type = type;
	}

	public List<ConferenceMetaValue> getValues() {
		return values;
	}
}
