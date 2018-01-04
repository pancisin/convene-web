package com.pancisin.bookster.repository;

import java.util.UUID;

import com.pancisin.bookster.model.Article;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Override
	@Cacheable("articles")
	Article findOne(Long id);

	@Override
	@CacheEvict(value = "articles", key = "#p0.id")
	<S extends Article> S save(S entity);

	@Override
	@CacheEvict(value = "articles", key = "#p0.id")
	void delete(Long id);

	@Query("SELECT article FROM Article article JOIN article.conference conference WHERE conference.id = :conference_id")
	public Page<Article> getByConference(@Param("conference_id") Long conference_id, Pageable pageable);

	@Query("SELECT article FROM Article article WHERE article.articlesList.id = :articles_list_id")
	public Page<Article> getByArticlesList(@Param("articles_list_id") UUID articles_list_id, Pageable pageable);
}
