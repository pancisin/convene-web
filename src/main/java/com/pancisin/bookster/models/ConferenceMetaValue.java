package com.pancisin.bookster.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

@Entity
@Table(name = "conference_meta_values")
public class ConferenceMetaValue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JsonRawValue
	private ConferenceMetaField field;
	
	@JsonIgnore
	@ManyToOne
	private ConferenceAttendee attendee;
	
	@Column
	private String value;

	public ConferenceMetaField getField() {
		return field;
	}

	public void setField(ConferenceMetaField field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public ConferenceAttendee getAttendee() {
		return attendee;
	}

	public void setAttendee(ConferenceAttendee attendee) {
		this.attendee = attendee;
	}
}
