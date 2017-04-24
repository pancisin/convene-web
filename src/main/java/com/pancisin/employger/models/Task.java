package com.pancisin.employger.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.pancisin.employger.rest.controllers.objects.Attribute;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	private List<AttributeOption> attributeOptions;

	@JsonIgnore
	@ManyToOne
	private Duty duty;

	@Column
	private String note;

	@Transient
	private List<Attribute> attributes;

	public List<Attribute> getAttributes() {
		if (attributes != null) return attributes;
		return attributeOptions.stream().collect(Collectors.groupingBy(a -> a.getAttribute())).entrySet().stream()
				.map(e -> {
					return new Attribute(e.getValue(), e.getKey());
				}).collect(Collectors.toList());
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
		attributeOptions = new ArrayList<AttributeOption>();
		attributes.stream().forEach(a -> attributeOptions.addAll(a.getValues()));
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public List<AttributeOption> getAttributeOptions() {
		return attributeOptions;
	}

	public void setAttributeOptions(List<AttributeOption> attributeOptions) {
		this.attributeOptions = attributeOptions;
	}

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
