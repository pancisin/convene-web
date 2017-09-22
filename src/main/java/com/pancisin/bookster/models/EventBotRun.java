package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "event_bots_runs")
public class EventBotRun {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar date;

	@Column
	private boolean success = false;

	@Column
	private int eventsCount;
	
	@JsonIgnore
	@ManyToOne(optional = false)
	private EventBot bot;

	public EventBotRun() {
		
	}
	
	public EventBotRun(EventBot bot, boolean success, int eventsCount) {
		this.bot = bot;
		this.success = success;
		this.eventsCount = eventsCount;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public EventBot getBot() {
		return bot;
	}

	public void setBot(EventBot bot) {
		this.bot = bot;
	}

	public UUID getId() {
		return id;
	}

	public Calendar getDate() {
		return date;
	}

	public int getEventsCount() {
		return eventsCount;
	}

	public void setEventsCount(int eventsCount) {
		this.eventsCount = eventsCount;
	}
}
