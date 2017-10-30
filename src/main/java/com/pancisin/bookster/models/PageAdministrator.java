package com.pancisin.bookster.models;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.Role;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "pages_administrators", uniqueConstraints = { @UniqueConstraint(columnNames = { "page_id", "user_id" }) })
public class PageAdministrator {

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonView(Summary.class)
	@JsonIgnoreProperties({"address", "license", "role", "authorities", "created", "token", "locale"})
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@JsonIgnore
	@ManyToOne(optional = false)
	private Page page;

	@NotNull
	@JsonView(Summary.class)
	@Column
	private boolean active = false;

	@JsonView(Summary.class)
	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@NotNull
	@JsonView(Summary.class)
	@Enumerated(EnumType.STRING)
	private PageRole role = PageRole.ROLE_ADMINISTRATOR;

	public PageAdministrator() {

	}

	public PageAdministrator(Page page, User user, boolean active) {
		this.page = page;
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
		return created;
	}

	public PageRole getRole() {
		return role;
	}

	public void setRole(PageRole role) {
		this.role = role;
	}
}
