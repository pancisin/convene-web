package com.pancisin.bookster.models;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pancisin.bookster.models.views.Summary;

@Entity
@Table(name = "medias")
public class Media {

	@Id
	@JsonView(Summary.class)
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar created;
	
	@Column
	@JsonView(Summary.class)
	private String title;

	@Column
	private String description;
	
	@Column
	@JsonView(Summary.class)
	private String path;
	
	@Column
	private boolean deleted;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String data;
	
	@JsonIgnore
	@JsonProperty(access = Access.READ_ONLY)
	@ManyToOne(optional = true)
	private User author;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Long size;
	
	public Media() {
		
	}
	
	public Media(String path) {
		this.path = path;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UUID getId() {
		return id;
	}

	public Calendar getCreated() {
		return created;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
}
