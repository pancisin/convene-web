package com.pancisin.bookster.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String state;

	@Column
	private String city;

	@Column
	private String street;

	@Column
	private String number;

	@Column
	private String zip;

	@Column
	private String formatted;

	@OneToOne(mappedBy = "address")
	private Place place;

	@Column(precision = 10, scale = 8)
	private BigDecimal latitude;

	@Column(precision = 10, scale = 8)
	private BigDecimal longitude;

	public String getState() {
		return state;
	}

	@JsonIgnore
	public com.paylane.client.api.models.Address getPaylaneAddress() {
		return new com.paylane.client.api.models.Address(this.street + " " + this.number, this.city, this.state,
				this.zip, "SK");
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getFormatted() {
		return formatted;
	}

	public void setFormatted(String formatted) {
		this.formatted = formatted;
	}
}
