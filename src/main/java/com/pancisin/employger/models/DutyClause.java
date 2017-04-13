package com.pancisin.employger.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity()
@Table(name = "clauses")
public class DutyClause {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne
	private Duty duty;
	
	@Column(name = "primary_date")
	private Date primaryDate;
	
	@Column(name = "alternative_date")
	private Date alternativeDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPrimaryDate() {
		return primaryDate;
	}

	public void setPrimaryDate(Date primaryDate) {
		this.primaryDate = primaryDate;
	}

	public Date getAlternativeDate() {
		return alternativeDate;
	}

	public void setAlternativeDate(Date alternativeDate) {
		this.alternativeDate = alternativeDate;
	}

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}
}
