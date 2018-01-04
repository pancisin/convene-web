package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.pancisin.bookster.model.Administrator;
import com.pancisin.bookster.model.Article;
import com.pancisin.bookster.model.Media;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.model.Activity;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.PageState;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.models.interfaces.IAuthor;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "conferences")
public class Conference implements IAuthor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<Administrator> administrators;

	@Column
	private String name;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	private List<MetaField> metaFields;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Survey> surveys;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<Event> events;

	@Enumerated(EnumType.STRING)
	private Visibility visibility;

	@JsonView(Summary.class)
	@Enumerated(EnumType.STRING)
	private PageState state = PageState.DEACTIVATED;

	@Lob
	@Column
	private String summary;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<Invitation> invitations;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<ConferenceAttendee> attendees;

	@OneToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JsonView(Summary.class)
	private Media poster;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String posterData;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<Article> articles;

	@JsonIgnore
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH }, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Place> places;

	@JsonIgnore
	public User getOwner() {
		Optional<Administrator> owner = this.administrators.stream()
				.filter(x -> x.getRole() == PageRole.ROLE_OWNER).findFirst();

		if (owner.isPresent())
			return owner.get().getUser();

		return null;
	}

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "conference")
	private List<Activity> activities;

	public void addActivity(Activity activity) {
		if (this.activities == null)
			this.activities = new ArrayList<Activity>();

		this.activities.add(activity);
	}

	@JsonIgnore
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Widget> widgets;

	public void addArticle(Article article) {
		if (this.articles == null)
			this.articles = new ArrayList<Article>();

		this.articles.add(article);
	}

	@Transient
	@JsonView(Summary.class)
	@JsonIgnoreProperties({"user"})
	public Administrator getPrivilege() {
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (this.administrators == null || user == null)
				return null;

			Optional<Administrator> pUser = this.administrators.stream()
					.filter(x -> x.getUser().getId() == user.getId()).findFirst();

			if (pUser.isPresent())
				return pUser.get();
		}

		return null;
	}

	public void addMetaField(MetaField field) {
		if (this.metaFields == null)
			this.metaFields = new ArrayList<MetaField>();

		this.metaFields.add(field);
	}

	public void addSurvey(Survey survey) {
		if (this.surveys == null)
			this.surveys = new ArrayList<Survey>();

		this.surveys.add(survey);
	}

	public void addPlace(Place place) {
		if (this.places == null)
			this.places = new ArrayList<Place>();

		this.places.add(place);
	}

	public int getAttendeesCount() {
		if (this.attendees != null)
			return this.attendees.size();
		else
			return 0;
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

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public String getType() {
		return "conference";
	}

	public List<Invitation> getInvitations() {
		return invitations;
	}

	public List<Administrator> getAdministrators() {
		return administrators;
	}

	public List<ConferenceAttendee> getAttendees() {
		return attendees;
	}

	public List<MetaField> getMetaFields() {
		return metaFields;
	}

	public List<Survey> getSurveys() {
		return surveys;
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

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
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

	public List<Place> getPlaces() {
		return places;
	}
}
