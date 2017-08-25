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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
	@OneToMany(mappedBy = "conference", cascade = CascadeType.REMOVE)
	private List<ConferenceAdministrator> conferenceAdministrators;

	@Column
	private String name;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	private List<MetaField> metaFields;
	
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
	
	@Column
	private String bannerUrl;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<Article> articles;
	
	@JsonIgnore
	public User getOwner() {
		Optional<ConferenceAdministrator> owner = this.conferenceAdministrators.stream()
				.filter(x -> x.getRole() == PageRole.ROLE_OWNER).findFirst();

		if (owner.isPresent())
			return owner.get().getUser();

		return null;
	}
	
	public void addArticle(Article article) {
		if (this.articles == null)
			this.articles = new ArrayList<Article>();
		
		this.articles.add(article);
	}
	
	public void addMetaField(MetaField field) {
		if (this.metaFields == null)
			this.metaFields = new ArrayList<MetaField>();
		
		this.metaFields.add(field);
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

	public List<ConferenceAdministrator> getConferenceAdministrators() {
		return conferenceAdministrators;
	}

	public List<ConferenceAttendee> getAttendees() {
		return attendees;
	}

	public List<MetaField> getMetaFields() {
		return metaFields;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
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
}
