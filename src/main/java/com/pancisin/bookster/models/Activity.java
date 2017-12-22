package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pancisin.bookster.models.enums.ActivityType;

@Entity
@Table(name = "activities")
public class Activity {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(optional = true)
	@JsonSerialize(using = ToStringSerializer.class)
	private User user;

	@Column
	@Enumerated(EnumType.STRING)
	private ActivityType type;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@JsonIgnore
	@ManyToOne
	@JoinTable(name = "conferences_activities", joinColumns = @JoinColumn(name = "activity_id"), inverseJoinColumns = @JoinColumn(name = "conference_id"))
	private Conference conference;
	
	@JsonIgnore
	@ManyToOne
	@JoinTable(name = "pages_activities", joinColumns = @JoinColumn(name = "activity_id"), inverseJoinColumns = @JoinColumn(name = "page_id"))
	private Page page;
	
	public Activity() {

	}

	public Activity(User user, ActivityType type) {
		this.user = user;
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public ActivityType getType() {
		return type;
	}

	public Calendar getCreated() {
		return created;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
