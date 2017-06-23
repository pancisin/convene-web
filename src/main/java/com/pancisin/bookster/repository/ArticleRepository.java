package com.pancisin.bookster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	
}
