package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.pancisin.bookster.models.enums.BotSourceType;

@Entity
@Table(name = "article_bots")
public class ArticleBot {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@ElementCollection
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	@CollectionTable(name = "article_bots_parsers", joinColumns = { @JoinColumn(name = "article_bot_id") })
	private Map<String, String> parser = new HashMap<String, String>();

	@ManyToOne(optional = false)
	private ArticlesList articlesList;

	@Column
	private String name;
	
	@Column
	private String sourceUrl;

	@Enumerated(EnumType.STRING)
	private BotSourceType sourceType = BotSourceType.REST_API;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@Column
	private boolean active;

	public Map<String, String> getParser() {
		return parser;
	}

	public void setParser(Map<String, String> parser) {
		this.parser = parser;
	}

	public ArticlesList getArticlesList() {
		return articlesList;
	}

	public void setArticlesList(ArticlesList articlesList) {
		this.articlesList = articlesList;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public BotSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(BotSourceType sourceType) {
		this.sourceType = sourceType;
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

	public Calendar getCreated() {
		return created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
