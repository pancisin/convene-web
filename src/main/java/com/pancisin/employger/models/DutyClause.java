package com.pancisin.employger.models;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity()
@Table(name = "clauses", uniqueConstraints = { @UniqueConstraint(columnNames = { "primary_date", "duty_id" }) })
public class DutyClause {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne
	private Duty duty;

	@Column(name = "primary_date")
	private Calendar primaryDate;

	@Column(name = "alternative_date")
	private Calendar alternativeDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getPrimaryDate() {
		return primaryDate;
	}

	public void setPrimaryDate(Calendar primaryDate) {
		this.primaryDate = primaryDate;
	}

	public Calendar getAlternativeDate() {
		return alternativeDate;
	}

	public void setAlternativeDate(Calendar alternativeDate) {
		this.alternativeDate = alternativeDate;
	}

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}
}
