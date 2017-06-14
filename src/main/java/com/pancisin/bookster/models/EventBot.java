package com.pancisin.bookster.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.views.Compact;

@Entity
@Table(name = "eventBots")
public class EventBot {

	@Id
	@JsonView(Compact.class)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String fbPageId;
	
	@JsonIgnore
	@ManyToOne(optional = true)
	private User alias;
	
	@JsonIgnore
	@ManyToOne(optional = true)
	private Page page;

	public String getFbPageId() {
		return fbPageId;
	}

	public void setFbPageId(String fbPageId) {
		this.fbPageId = fbPageId;
	}

	public User getAlias() {
		return alias;
	}

	public void setAlias(User alias) {
		this.alias = alias;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Long getId() {
		return id;
	}
}
