package com.pancisin.employger.rest.controllers.objects;

import java.util.List;

import com.pancisin.employger.models.AttributeOption;

public class Attribute {

	private Long id;
	private String name;
	private List<AttributeOption> values;

	public Attribute(List<AttributeOption> values, com.pancisin.employger.models.Attribute attribute) {
		this.values = values;
		this.id = attribute.getId();
		this.name = attribute.getName();
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
