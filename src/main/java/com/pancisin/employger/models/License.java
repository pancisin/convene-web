package com.pancisin.employger.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "licenses")
public class License {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	private Company company;

	@Column
	private Timestamp acquired;
	
	@Column
	private Timestamp expires;

	@Enumerated(EnumType.STRING)
	private LicenseStatus status;
	
	public Long getId() {
		return id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Timestamp getAcquired() {
		return acquired;
	}

	public void setAcquired(Timestamp acquired) {
		this.acquired = acquired;
	}

	public Timestamp getExpires() {
		return expires;
	}

	public void setExpires(Timestamp expires) {
		this.expires = expires;
	}

	public LicenseStatus getStatus() {
		return status;
	}

	public void setStatus(LicenseStatus status) {
		this.status = status;
	}
}