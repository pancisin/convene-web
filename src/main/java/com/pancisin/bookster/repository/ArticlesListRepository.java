package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.models.ArticlesList;

public interface ArticlesListRepository extends JpaRepository<ArticlesList, UUID> {

	public ArticlesList findByTagsHash(int tagsHash);
}
