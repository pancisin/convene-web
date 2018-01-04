package com.pancisin.bookster.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.model.ArticleBot;

@Repository
public interface ArticleBotRepository extends JpaRepository<ArticleBot, UUID> {

	@Query("SELECT bot FROM ArticleBot bot WHERE bot.articlesList.id = :articlesListId")
	public Page<ArticleBot> getByList(@Param("articlesListId") UUID ArticlesListId, Pageable pageable);
}
