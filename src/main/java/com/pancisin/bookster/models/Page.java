package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.PageState;
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
	@OneToMany(mappedBy = "page")
	private List<PageAdministrator> pageAdministrators;

	@Column
	@JsonView(Compact.class)
	private String name;

	@JsonView(Compact.class)
	private String slug;

	@Lob
	@Column
	private String summary = "";

	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Event> events;

	@ManyToOne
	@JsonView(Summary.class)
	private Branch branch;

	@JsonIgnore
	@ManyToMany
	private List<User> followers = new ArrayList<User>();

	@JsonIgnore
	@OneToMany(mappedBy = "page")
	private List<Service> services;

	@OneToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JsonView(Summary.class)
	private Media poster;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String posterData;

	@JsonView(Summary.class)
	public int getFollowersCount() {
		return this.followers.size();
	}

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@JsonIgnore
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH }, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Place> places;

	@JsonIgnore
	public User getOwner() {
		Optional<PageAdministrator> owner = this.pageAdministrators.stream().filter(x -> x.getRole() == PageRole.ROLE_OWNER)
				.findFirst();

		if (owner.isPresent())
			return owner.get().getUser();

		return null;
	}

	@JsonView(Summary.class)
	@Enumerated(EnumType.STRING)
	private PageState state = PageState.DEACTIVATED;

	@JsonView(Summary.class)
	public Category getCategory() {
		if (this.getBranch() != null)
			return this.getBranch().getCategory();

		return null;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Activity> activities;

	public void addActivity(Activity activity) {
		if (this.activities == null)
			this.activities = new ArrayList<Activity>();

		this.activities.add(activity);
	}

	@JsonIgnore
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Widget> widgets;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST })
	private List<Media> gallery;

	@Transient
	@JsonView(Summary.class)
	public PageAdministrator getPrivilege() {
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (this.pageAdministrators == null || user == null)
				return null;

			Optional<PageAdministrator> pUser = this.pageAdministrators.stream()
					.filter(x -> x.getUser().getId() == user.getId()).findFirst();

			if (pUser.isPresent())
				return pUser.get();
		}

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

	public Media getPoster() {
		return poster;
	}

	public void setPoster(Media poster) {
		this.poster = poster;
	}

	public String getPosterData() {
		return posterData;
	}

	public void setPosterData(String posterData) {
		this.posterData = posterData;
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

	@Override
	public String getType() {
		return "page";
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public PageState getState() {
		return state;
	}

	public void setState(PageState state) {
		this.state = state;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<Widget> widgets) {
		if (this.widgets == null) {
			this.widgets = new ArrayList<Widget>();
		}

		this.widgets.clear();

		if (widgets != null) {
			this.widgets.addAll(widgets);
		}
	}

	public void addPlace(Place place) {
		if (this.places == null)
			this.places = new ArrayList<Place>();

		this.places.add(place);
	}

	public List<Media> getGallery() {
		return gallery;
	}

	public void AddGallery(Media media) {
		if (this.gallery == null) {
			this.gallery = new ArrayList<Media>();
		}

		this.gallery.add(media);
	}
}
