package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.pancisin.bookster.models.enums.BotRunState;

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
	@JsonProperty(access = Access.READ_ONLY)
	@Enumerated(EnumType.STRING)
	private BotRunState state = BotRunState.SCHEDULED;

	@Column
	private int eventsCount;

	@JsonIgnoreProperties({"fbPageId", "active", "ceated", "lastRun", "runsCount" })
	@ManyToOne(optional = false)
	private EventBot bot;

	public EventBotRun() {

	}

	public EventBotRun(EventBot bot, int eventsCount) {
		this.bot = bot;
		this.eventsCount = eventsCount;
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

	public BotRunState getState() {
		return state;
	}

	public void setState(BotRunState state) {
		this.state = state;
	}
}
