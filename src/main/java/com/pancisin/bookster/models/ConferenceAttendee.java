package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;

@Entity
@Table(name = "conferences_attendees", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "conference_id", "user_id" }) })
public class ConferenceAttendee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User user;

	@JsonIgnore
	@ManyToOne
	private Conference conference;

	@OneToMany(cascade = CascadeType.ALL)
	private List<MetaValue> meta;

	@Column
	private boolean active = true;

	@Column
	private boolean approved;

	@JsonView(Compact.class)
	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	public ConferenceAttendee() {

	}

	public ConferenceAttendee(User user, Conference conference, List<MetaValue> meta) {
		this.user = user;
		this.conference = conference;
		this.meta = meta;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public List<MetaValue> getMeta() {
		return meta;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setMeta(List<MetaValue> meta) {
		this.meta = meta;
	}
}
