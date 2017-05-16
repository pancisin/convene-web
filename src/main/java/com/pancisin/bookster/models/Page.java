package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "pages")
public class Page {

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@ManyToMany
	private List<User> administrators = new ArrayList<User>();
	
	@Column
	@JsonView(Compact.class)
	private String name;
	
	@Lob
	@Column
	private String summary;
	
	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Event> events;
	
	@ManyToOne
	private Branch category;
	
	@JsonIgnore
	@ManyToMany
	private List<User> followers = new ArrayList<User>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Service> services;
	
	@Column
	@JsonView(Summary.class)
	private String bannerUrl;
	
	@JsonView(Summary.class)
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

	public Branch getCategory() {
		return category;
	}

	public void setCategory(Branch category) {
		this.category = category;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public List<Service> getServices() {
		return services;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
}
