package com.pancisin.employger.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "companies")
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;
	
	@Column(unique = true)
	private String ico;

	@Column()
	private String logo;
	
	@JsonIgnore
	@OneToMany(mappedBy = "company")
	private List<License> licenses;
	
	@JsonIgnore
	@OneToMany(mappedBy = "company")
	private List<Employee> employees;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public Long getId() {
		return id;
	}

	public List<License> getLicenses() {
		return licenses;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
