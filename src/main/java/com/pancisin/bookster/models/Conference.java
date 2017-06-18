package com.pancisin.bookster.models;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.models.interfaces.IAuthor;

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
	@OneToMany(mappedBy = "conference")
	private List<Event> events;

	@Enumerated(EnumType.STRING)
	private Visibility visibility;

	@Lob
	@Column
	private String summary;

	@JsonIgnore
	@OneToMany(mappedBy = "conference")
	private List<Invitation> invitations;

	@JsonIgnore
	public User getOwner() {
		Optional<ConferenceAdministrator> owner = this.conferenceAdministrators.stream()
				.filter(x -> x.getRole() == PageRole.ROLE_OWNER).findFirst();

		if (owner.isPresent())
			return owner.get().getUser();

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
}
