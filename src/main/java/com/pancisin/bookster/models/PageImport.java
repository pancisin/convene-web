package com.pancisin.bookster.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pancisin.bookster.models.enums.BotRunState;

@Entity
@Table(name = "pages_imports")
public class PageImport {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;
	
	@Transient
	private BotRunState state;
	
	@OneToOne
	@JsonIgnoreProperties({ "gallery", "metadata", "followersCount" })
	private Page page;
	
	@Column
	private String sourceName;
	
	@Column(unique = true)
	private String sourceId;
	
	public PageImport() {
		
	}
	
	public PageImport(BotRunState state) {
		this.state = state;
	}

	public PageImport(BotRunState state, Page page) {
		this.state = state;
		this.page = page;
	}
	
	public BotRunState getState() {
		return state;
	}

	public void setState(BotRunState state) {
		this.state = state;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public UUID getId() {
		return id;
	}
}

