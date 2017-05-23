package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
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
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.interfaces.IAuthor;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "pages")
public class Page implements IAuthor {

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy = "page", cascade = CascadeType.REMOVE)
	private List<PageAdministrator> pageAdministrators;

	@Column
	@JsonView(Compact.class)
	private String name;

	@Lob
	@Column
	private String summary = "";
	
	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Event> events;

	@ManyToOne
	private Branch branch;

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

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Place> places;
	
	public Category getCategory() {
		if (getBranch() != null)
			return getBranch().getCategory();

		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
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

	public List<PageAdministrator> getPageAdministrators() {
		return pageAdministrators;
	}

	public void setPageAdministrators(List<PageAdministrator> pageAdministrators) {
		this.pageAdministrators = pageAdministrators;
	}

	public List<Place> getPlaces() {
		return places;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}
}
