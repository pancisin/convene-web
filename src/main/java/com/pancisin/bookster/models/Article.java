package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "articles")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;

	@Column
	private Calendar date;

	@Column
	private String title;

	@Lob
	@Column
	private String content;

	@OneToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JsonView(Summary.class)
	private Media thumbnail;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String thumbnailData;

	@ManyToOne
	@JsonProperty(access = Access.READ_ONLY)
	private User author;

	@Column
	private boolean published;

	@JsonIgnore
	@ManyToOne(optional = true)
	@JoinTable(name = "conferences_articles", joinColumns = @JoinColumn(name = "articles_id"), inverseJoinColumns = @JoinColumn(name = "conference_id"))
	private Conference conference;

	@Column(unique = true, updatable = false, nullable = false)
	private int identifier;
	
	@JsonIgnore
	@ManyToOne(optional = true)
	@JoinTable(name = "articles_lists_articles", joinColumns = @JoinColumn(name = "articles_id"), inverseJoinColumns = @JoinColumn(name = "articles_list_id"))
	private ArticlesList articlesList;
	
	@PrePersist
	private void onCreate() {
		List<String> values = new ArrayList<String>();
		values.add(this.title);
		
		if (this.articlesList != null) {
			values.add(this.articlesList.getId().toString());
		} else if (this.conference != null) {
			values.add(this.conference.getId().toString());
		}
		
		int result = 17;
	  for (String v : values) {
	  	result = 37 * result + v.hashCode();
	  }
	  
	  this.identifier = result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Media getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Media thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getThumbnailData() {
		return thumbnailData;
	}

	public void setThumbnailData(String thumbnailData) {
		this.thumbnailData = thumbnailData;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Long getId() {
		return id;
	}

	public Calendar getCreated() {
		return created;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public int getIdentifier() {
		return identifier;
	}

	public ArticlesList getArticlesList() {
		return articlesList;
	}

	public void setArticlesList(ArticlesList articlesList) {
		this.articlesList = articlesList;
	}
}
