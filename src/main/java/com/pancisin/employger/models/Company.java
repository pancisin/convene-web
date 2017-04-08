package com.pancisin.employger.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;

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
	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private List<License> licenses;
	
	@JsonIgnore
	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private List<Employee> employees;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address address = new Address();
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Email
	@Column(name = "email")
	private String email;
	
	@JsonIgnore
	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private List<User> users;
	
	@JsonIgnore
	@OneToMany(mappedBy = "company")
	private List<Duty> duties;
	
	@JsonIgnore
	@OneToMany(mappedBy = "company")
	private List<Customer> customers;
	
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Duty> getDuties() {
		return duties;
	}

	public void setDuties(List<Duty> duties) {
		this.duties = duties;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
}
