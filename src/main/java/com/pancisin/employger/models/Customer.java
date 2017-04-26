package com.pancisin.employger.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.employger.repository.DutyClauseRepository;
import com.pancisin.employger.rest.controllers.objects.DutyInstance;

@Entity()
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne
	private Company company;

	@NotEmpty
	@Column
	private String name;

	@Column
	private String description;

	@Column
	private String person;

	@Email
	@NotEmpty
	@Column
	private String email;

	@Column
	private String address;

	@JsonIgnore
	@OneToMany(mappedBy = "customer")
	private List<Duty> duties;

	@OneToOne(cascade = CascadeType.ALL)
	private Report report = new Report();

//	@Autowired
//	private DutyClauseRepository clauseRepository;

	public List<DutyInstance> getDutyInstances(Date date_from, Date date_to) {
//		List<DutyClause> clauses = clauseRepository.getRelated(date_from, date_to);

		List<DutyClause> clauses = new ArrayList<DutyClause>();
		
		return this.duties.stream().flatMap(duty -> duty.getOcurrencesInRange(date_from, date_to).stream().map(occ -> {
			Calendar cal = Calendar.getInstance();
			cal.setTime(occ);

			Optional<DutyClause> clause = clauses.stream().filter(dc -> dc.getPrimaryDate().equals(cal)).findFirst();

			return new DutyInstance(duty, clause.isPresent() ? clause.get().getAlternativeDate() : cal,
					clause.isPresent() ? clause.get() : null);
		})).collect(Collectors.toList());
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Duty> getDuties() {
		return duties;
	}

	public void setDuties(List<Duty> duties) {
		this.duties = duties;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
}
