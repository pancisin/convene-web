package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.ArticlesList;

public interface ArticlesListRepository extends JpaRepository<ArticlesList, UUID> {

	@Override
	@Cacheable("articlesLists")
	ArticlesList findOne(UUID id);

	@Override
	@CacheEvict(value = "articlesLists", key = "#p0.id")
	<S extends ArticlesList> S save(S entity);

	@Cacheable("articlesLists")
	public ArticlesList findByTagsHash(int tagsHash);
}
