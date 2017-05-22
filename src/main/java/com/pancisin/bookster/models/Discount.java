package com.pancisin.bookster.models;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "discounts")
public class Discount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	private Service service;
	
	@Column
	private Calendar starts;
	
	@Column
	private Calendar ends;
	
	@Column
	private int percentage;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Calendar getStarts() {
		return starts;
	}

	public void setStarts(Calendar starts) {
		this.starts = starts;
	}

	public Calendar getEnds() {
		return ends;
	}

	public void setEnds(Calendar ends) {
		this.ends = ends;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public Long getId() {
		return id;
	}
}
