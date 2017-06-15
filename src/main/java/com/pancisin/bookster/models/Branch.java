package com.pancisin.bookster.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "branches")
public class Branch {

	@Id
	@JsonView(Summary.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String name;
	
	@JsonIgnore
	@ManyToOne
	private Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy = "branch")
	private List<Page> pages;

	@JsonView(Summary.class)
	@Column
	private String code;
	
	public String getName() {
		return name;
	}

	public Category getCategory() {
		return category;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}
}
