package com.pancisin.bookster.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.pancisin.bookster.model.MetaField;

@Entity
@Table(name = "meta_values")
public class MetaValue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JsonRawValue
	private MetaField field;

	@Column
	private String value;

	public MetaField getField() {
		return field;
	}

	public void setField(MetaField field) {
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
}
