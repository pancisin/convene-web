package com.pancisin.bookster.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "category")
	private List<Branch> branches;
	
	@Column
	private String code;
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Branch> getBranches() {
		return branches;
	}

	public String getCode() {
		return code;
	}
}
