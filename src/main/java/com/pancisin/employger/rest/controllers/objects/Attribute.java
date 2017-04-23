package com.pancisin.employger.rest.controllers.objects;

import java.util.List;

import com.pancisin.employger.models.AttributeOption;

public class Attribute {

	private Long id;
	private String name;
	private List<AttributeOption> values;

	public Attribute() {
		
	}
	
	public Attribute(List<AttributeOption> values, com.pancisin.employger.models.Attribute attribute) {
		this.values = values;
		this.id = attribute.getId();
		this.name = attribute.getName();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValues(List<AttributeOption> values) {
		this.values = values;
	}

	public List<AttributeOption> getValues() {
		return values;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
