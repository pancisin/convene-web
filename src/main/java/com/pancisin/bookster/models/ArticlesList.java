package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "articles_list")
public class ArticlesList {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@JsonIgnore
	@ManyToMany(mappedBy = "articlesList")
	private List<Article> articles;

	@Column
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "articlesList")
	private List<ArticleBot> bots;

	@Column(unique = true, nullable = false)
	private int tagsHash;

	@ElementCollection
	@CollectionTable(name = "tags")
	@JsonProperty
	private List<String> tags;

	@PreUpdate
	private void onUpdate() {
		if (this.tags != null && !this.tags.isEmpty()) {
			int result = 17;
			for (String v : this.tags) {
				result = 37 * result + v.hashCode();
			}

			this.tagsHash = result;
		}
	}

	public void addArticle(Article article) {
		if (this.getArticles() == null) {
			this.articles = new ArrayList<Article>();
		}

		this.getArticles().add(article);
	}

	public void addArticles(List<Article> articles) {
		if (this.getArticles() == null) {
			this.articles = new ArrayList<Article>();
		}

		this.getArticles().addAll(articles);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public int getTagsHash() {
		return tagsHash;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getTags() {
		return tags;
	}
}