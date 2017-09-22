package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;

@Entity
@Table(name = "eventBots")
public class EventBot {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;
	
	@Column
	private String fbPageId;
	
	@JsonIgnore
	@ManyToOne
	private Page page;

	@Column
	private boolean active = false;
	
	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@JsonIgnore
	@OneToMany(mappedBy = "bot", orphanRemoval = true)
	private List<EventBotRun> runs; 
	
	public int getRunsCount() {
		return runs != null ? runs.size() : 0;
	}
	
	public Calendar getLastRun() {
		return this.runs != null && this.runs.size() > 0 ? runs.stream().reduce((a, b) -> a.getDate().compareTo(b.getDate()) > 0 ? a : b).get().getDate() : null;
	}
	
	public String getFbPageId() {
		return fbPageId;
	}

	public void setFbPageId(String fbPageId) {
		this.fbPageId = fbPageId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UUID getId() {
		return id;
	}

	public List<EventBotRun> getRuns() {
		return runs;
	}
}
