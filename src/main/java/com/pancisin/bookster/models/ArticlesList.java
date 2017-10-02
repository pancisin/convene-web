package com.pancisin.bookster.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "articles_list")
public class ArticlesList {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
	private UUID id;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Article> articles = new ArrayList<Article>();

	@Column
	private String name;

	// TODO: Should have parsers entities mounted/merged relations.
	// TODO: Should contain list attributes as language, category, tags...

	public void addArticle(Article article) {
		this.articles.add(article);
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
}