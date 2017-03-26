package com.pancisin.employger.models;

import java.sql.Date;
import java.util.Calendar;
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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.employger.repository.converters.ScheduleConverter;

@Entity
@Table(name = "duties")
public class Duty {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany(mappedBy = "duties", cascade = CascadeType.ALL)
	private List<Employee> employees;

	@NotNull
	@Column(name = "location")
	private String location;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "recurrence")
	@Convert(converter = ScheduleConverter.class)
	private Schedule recurrence;

	@OneToMany(mappedBy = "duty", cascade= CascadeType.ALL)
	private List<Task> tasks;

	@JsonIgnore
	@ManyToOne
	private Company company;
	
	@Column(name="created", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Calendar created;
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Duty))
			return false;
		Duty duty = (Duty) obj;
		return this.id.equals(duty.id);
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public Schedule getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(Schedule recurrence) {
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
}
