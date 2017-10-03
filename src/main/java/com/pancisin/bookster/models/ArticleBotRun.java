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
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.models.enums.BotRunState;

@Entity
@Table(name = "article_bots_runs")
public class ArticleBotRun {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	private Calendar date;

	@Column
	@JsonProperty(access = Access.READ_ONLY)
	@Enumerated(EnumType.STRING)
	private BotRunState state = BotRunState.SCHEDULED;

	@Column
	private int articlesCount;
	
	@JsonIgnoreProperties({"active", "ceated", "lastRun", "runsCount" })
	@ManyToOne(optional = false)
	private ArticleBot bot;
	
	public ArticleBotRun() {

	}

	public ArticleBotRun(ArticleBot bot, BotRunState state) {
		this.bot = bot;
		this.state = state;
	}
	
	@PrePersist
	private void onCreate() {
		this.date = Calendar.getInstance();
	}

	public BotRunState getState() {
		return state;
	}

	public void setState(BotRunState state) {
		this.state = state;
	}

	public int getArticlesCount() {
		return articlesCount;
	}

	public void setArticlesCount(int articlesCount) {
		this.articlesCount = articlesCount;
	}

	public UUID getId() {
		return id;
	}

	public Calendar getDate() {
		return date;
	}

	public ArticleBot getBot() {
		return bot;
	}
}
