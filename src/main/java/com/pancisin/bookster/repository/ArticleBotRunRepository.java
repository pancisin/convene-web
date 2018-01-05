package com.pancisin.bookster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.bookster.model.ArticleBotRun;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ArticleBotRunRepository extends JpaRepository<ArticleBotRun, Long> {

  @Query("SELECT run FROM ArticleBotRun run WHERE run.bot.id = :botId")
  Page<ArticleBotRun> getByArticleBot(@Param("botId") UUID botId, Pageable pageable);
}
