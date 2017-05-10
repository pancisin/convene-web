package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pages")
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@ManyToMany
	private List<User> administrators = new ArrayList<User>();
	
	@Column
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Event> events;
	
	@ManyToOne
	private Category category;
	
	@JsonIgnore
	@ManyToMany
	private List<User> followers;
	
	public int getFollowersCount() {
		return this.followers.size();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<User> getAdministrators() {
		return administrators;
	}

	public void setAdministrators(List<User> administrators) {
		this.administrators = administrators;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Event> getEvents() {
		return events;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
}
