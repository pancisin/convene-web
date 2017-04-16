package com.pancisin.employger.models;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.employger.models.enums.ReportInterval;
import com.pancisin.employger.repository.converters.CronConverter;

@Entity()
@Table(name = "reports")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "recurrence")
	@Convert(converter = CronConverter.class)
	private CronExpression recurrence;

	@JsonIgnore
	@OneToOne(mappedBy = "report")
	private Customer customer;
	
	@Column
	private boolean active;

	@Column
	@Enumerated(EnumType.STRING)
	private ReportInterval period;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CronExpression getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(CronExpression recurrence) {
		this.recurrence = recurrence;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ReportInterval getInterval() {
		return period;
	}

	public void setInterval(ReportInterval interval) {
		this.period = interval;
	}
}
