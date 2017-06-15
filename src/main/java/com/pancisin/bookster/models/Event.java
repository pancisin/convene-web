package com.pancisin.bookster.models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.models.interfaces.IAuthor;
import com.pancisin.bookster.models.views.Compact;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "events")
public class Event {

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	@NotEmpty
	@JsonView(Compact.class)
	private String name;

	@Column
	@NotNull
	@JsonView(Summary.class)
	private Calendar date;

	@JsonIgnore
	@ManyToOne
	private User owner;

	@NotNull
	@Enumerated(EnumType.STRING)
	@JsonView(Summary.class)
	private Visibility visibility;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@OneToMany(mappedBy = "event")
	@OrderBy("time")
	private List<Programme> programme;

	@JsonIgnore
	@ManyToOne
	private Conference conference;

	@ManyToOne
	@JsonIgnore
	private Page page;

	@Lob
	@Column
	private String summary;

	@Column(unique = true)
	private String facebookId;
	
	@JsonIgnore
	@ManyToMany
	private List<User> attendees = new ArrayList<User>();

	@Column
	@JsonView(Summary.class)
	private String bannerUrl;

	@OneToOne
	private Place place;

	@JsonProperty(required = false)
	public Time getStartsAt() {
		if (this.programme != null && this.programme.size() > 0) {
			Optional<Programme> first = this.programme.stream().min((p1, p2) -> {
				return p2.getTime().compareTo(p1.getTime());
			});

			if (first.isPresent()) {
				return first.get().getTime();
			}
		}

		return null;
	}

	public IAuthor getAuthor() {
		if (conference != null)
			return conference;
		else if (page != null)
			return page;

		return owner;
	}

	public int getAttendeesCount() {
		return this.attendees.size();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User creator) {
		this.owner = creator;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public List<Programme> getProgramme() {
		return programme;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public List<User> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<User> attendees) {
		this.attendees = attendees;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public void setProgramme(List<Programme> programme) {
		this.programme = programme;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
}
