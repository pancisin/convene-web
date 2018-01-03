package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.models.views.Summary;

@Entity
//@Table(name = "pages_administrators", uniqueConstraints = { @UniqueConstraint(columnNames = { "page_id", "user_id" }) })
@Table(name = "administrators")
public class Administrator {

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonView(Summary.class)
	@JsonIgnoreProperties({"address", "license", "authorities", "created", "token", "locale"})
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@JsonIgnore
	@ManyToOne
	@JoinTable(name = "conferences_administrators", joinColumns = @JoinColumn(name = "administrator_id"), inverseJoinColumns = @JoinColumn(name = "conference_id"))
	private Conference conference;
	
	@JsonIgnore
	@ManyToOne
	@JoinTable(name = "pages_administrators", joinColumns = @JoinColumn(name = "administrator_id"), inverseJoinColumns = @JoinColumn(name = "page_id"))
	private Page page;
	
	@NotNull
	@JsonView(Summary.class)
	@Column
	private boolean active = false;

	@JsonView(Summary.class)
	@Column(name = "granted", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar granted;
	
	@Column
	private Calendar expires;

	@NotNull
	@JsonView(Summary.class)
	@Enumerated(EnumType.STRING)
	private PageRole role = PageRole.ROLE_ADMINISTRATOR;

	public Administrator() {

	}

	public Administrator(Page page, User user, boolean active) {
		this.page = page;
		this.user = user;
		this.active = active;
	}
	
	public Administrator(Conference conference, User user, boolean active) {
		this.conference = conference;
		this.user = user;
		this.active = active;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public Calendar getCreated() {
		return granted;
	}

	public PageRole getRole() {
		return role;
	}

	public void setRole(PageRole role) {
		this.role = role;
	}
}
