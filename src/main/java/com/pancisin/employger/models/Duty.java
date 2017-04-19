package com.pancisin.employger.models;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.employger.repository.converters.CronConverter;

@Entity
@Table(name = "duties")
public class Duty {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany
	private List<Employee> employees;

	@NotNull
	@Column(name = "location")
	private String location;

	@ManyToOne
	private Customer customer;

	@Column(name = "start_date")
	private java.sql.Date startDate;

	@Column(name = "end_date")
	private java.sql.Date endDate;

	@Column(name = "recurrence")
	@Convert(converter = CronConverter.class)
	private CronExpression recurrence;

	@Column
	private String description;

	@Transient
	public String getCronRecurrence() {
		return recurrence.toString();
	}

	@OneToMany(mappedBy = "duty", cascade = CascadeType.ALL)
	private List<Task> tasks;

	@JsonIgnore
	@ManyToOne
	private Company company;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@JsonIgnore
	@OneToMany(mappedBy = "duty")
	private List<DutyClause> clauses;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Duty))
			return false;
		Duty duty = (Duty) obj;
		return this.id.equals(duty.id);
	}

	@JsonIgnore
	public List<Date> getNextOcurrences(int count, Date currentIteration) throws ParseException {
		List<Date> ocurrences = new ArrayList<Date>();
		org.quartz.CronExpression cron = new org.quartz.CronExpression(this.getCronRecurrence());

		for (int i = 0; i < count; i++) {
			currentIteration = cron.getNextValidTimeAfter(currentIteration);
			ocurrences.add(currentIteration);
		}
		return ocurrences;
	}

	@JsonIgnore
	public List<Date> getOcurrencesInRange(Date start, Date end) {
		if (start.compareTo(end) > 0)
			throw new InvalidParameterException();

		List<Date> ocurrences = null;

		try {
			ocurrences = new ArrayList<Date>();
			org.quartz.CronExpression cron = new org.quartz.CronExpression(this.getCronRecurrence());
			Date iteration = start;

			do {
				iteration = cron.getNextValidTimeAfter(iteration);
				ocurrences.add(iteration);
			} while (iteration.compareTo(end) < 0);
		} catch (Exception ex) {

		}

		return ocurrences;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public java.sql.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}

	public java.sql.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Date endDate) {
		this.endDate = endDate;
	}

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

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Calendar getCreated() {
		return created;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<DutyClause> getClauses() {
		return clauses;
	}
}
