package com.pancisin.bookster.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pancisin.bookster.models.ArticleBot;

@Repository
public interface ArticleBotRepository extends JpaRepository<ArticleBot, UUID> {

	@Query("SELECT bot FROM ArticleBot bot WHERE bot.articlesList.id = :articlesListId")
	public List<ArticleBot> getByList(@Param("articlesListId") UUID ArticlesListId);
}
