package com.pancisin.bookster.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	
	@Query("SELECT article FROM Article article JOIN article.conference conference WHERE conference.id = :conference_id")
	public List<Article> getByConference(@Param("conference_id") Long conference_id);
}
